package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.data.models.Status
import com.example.newsapp.ui.base.BaseActivity
import com.example.newsapp.ui.base.ItemDecorator
import com.example.newsapp.ui.loader.CustomErrorListItemCreator
import com.example.newsapp.ui.loader.CustomLoadingListItemCreator
import com.example.newsapp.ui.news_detail.NewsDetailActivity
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.shimmer_placeholder.*
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener
import ru.alexbykov.nopaginate.paginate.NoPaginate
import javax.inject.Inject


class HomeActivity : BaseActivity(), OnLoadMoreListener, HomeAdapter.OnNewsClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeAdapter: HomeAdapter

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var paginate: NoPaginate
    private var isFirstLoad = true
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initVariables()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun initVariables() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.setLightMode(this)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun initialiseLayout() {
        layoutManager = LinearLayoutManager(this)
        rvNews.layoutManager = layoutManager
        rvNews.addItemDecoration(ItemDecorator(10))
        rvNews.adapter = homeAdapter

        paginate = NoPaginate.with(rvNews)
            .setLoadingTriggerThreshold(1)
            .setOnLoadMoreListener(this)
            .setCustomErrorItem(CustomErrorListItemCreator(true))
            .setCustomLoadingItem(CustomLoadingListItemCreator(true))
            .build()
    }

    private fun setListener() {
        homeAdapter.setOnNewsClickListener(this)

        swipeToRefreshNews.setOnRefreshListener {
            homeAdapter.clearList()
            homeViewModel.refresh()
            homeAdapter.notifyDataSetChanged()
            paginate.setNoMoreItems(false)
            isFirstLoad = true
            homeViewModel.getTopHeadlines("", null)
            swipeToRefreshNews.isRefreshing = false
        }
    }

    private fun observeData() {
        homeViewModel.newsLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    ivNoNews.visibility = View.GONE
                    updateLoadingState(true)
                    updateErrorState(false)
                    if (isFirstLoad) {
                        shimmerViewContainer.startShimmer()
                        rvNews.visibility = View.GONE
                        shimmerViewContainer.visibility = View.VISIBLE
                    }
                }
                Status.SUCCESS -> {
                    updateLoadingState(false)
                    updateErrorState(false)
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    rvNews.visibility = View.GONE
                    it.data?.let { resp ->
                        val list = resp.articles
                        val previousSize = homeAdapter.getCurrentListSize()
                        homeAdapter.addNewItems(list)

                        if (list.isEmpty()) {
                            hasLoadedAllItems()
                        }

                        if (isFirstLoad) {
                            isFirstLoad = false
                            homeAdapter.notifyDataSetChanged()
                        } else if (list.isNotEmpty()) {
                            homeAdapter.notifyItemRangeInserted(
                                previousSize,
                                homeAdapter.getCurrentListSize() - previousSize
                            )
                        }

                        if (homeAdapter.getCurrentListSize() == 0) {
                            rvNews.visibility = View.GONE
                            ivNoNews.visibility = View.VISIBLE
                        } else {
                            ivNoNews.visibility = View.GONE
                            rvNews.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    updateErrorState(true)
                    updateLoadingState(false)
                    shimmerViewContainer.visibility = View.GONE
                    ivNoNews.visibility = View.VISIBLE
                    it.message?.let { errorMsg ->
                        showErrorToast(errorMsg)
                    }
                }
            }
        })
    }

    private fun updateLoadingState(isLoading: Boolean) {
        if (::paginate.isInitialized)
            paginate.showLoading(isLoading)
    }

    private fun updateErrorState(isError: Boolean) {
        if (::paginate.isInitialized)
            paginate.showError(isError)
    }

    private fun hasLoadedAllItems() {
        if (::paginate.isInitialized)
            paginate.setNoMoreItems(true)
    }

    private fun scrollToTop() {
        if (::layoutManager.isInitialized)
            layoutManager.scrollToPosition(0)
    }

    override fun onLoadMore() {
        homeViewModel.getTopHeadlines("", null)
    }

    override fun onNewsClicked(
        newsImageView: ImageView,
        titleTextView: TextView,
        article: Article
    ) {

        val imagePair =
            Pair.create<View, String>(newsImageView, getString(R.string.image_transition_name))

        val textViewPair =
            Pair.create<View, String>(titleTextView, getString(R.string.title_transition_name))

        val activityOptions =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imagePair,
                textViewPair
            )

        startActivity(
            NewsDetailActivity.newIntent(this, gson.toJson(article)),
            activityOptions.toBundle()
        )
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}