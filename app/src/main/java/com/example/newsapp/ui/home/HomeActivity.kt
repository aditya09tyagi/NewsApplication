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
    private var isListLoaded = true
    private lateinit var layoutManager: LinearLayoutManager
    private var currentPageNo: Int = 1
    private var currentListSize: Int = 0
    private var totalListSize: Int = 0


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
            .setCustomErrorItem(CustomErrorListItemCreator(false))
            .setCustomLoadingItem(CustomLoadingListItemCreator(false))
            .build()

        homeViewModel.getTopHeadlines("", currentPageNo)
    }

    private fun setListener() {
        homeAdapter.setOnNewsClickListener(this)

        swipeToRefreshNews.setOnRefreshListener {
            currentPageNo = 1
            homeAdapter.clearList()
            isFirstLoad = true
            homeViewModel.getTopHeadlines("", currentPageNo)
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
                        totalListSize = resp.totalResults
                        currentListSize += list.size
                        val previousSize = homeAdapter.getCurrentListSize()
                        if (isFirstLoad)
                            homeAdapter.initNewsList(list)
                        else
                            homeAdapter.addNewItems(list)
                        currentPageNo++

                        if (list.isEmpty()) {
                            hasLoadedAllItems()
                            currentPageNo = 0
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
                        isListLoaded = true
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
                    rvNews.visibility = View.GONE
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
        if ((currentListSize != 0 || currentListSize <= totalListSize) && !isFirstLoad && isListLoaded){
            isListLoaded = false
            homeViewModel.getTopHeadlines("", currentPageNo)
        }
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
