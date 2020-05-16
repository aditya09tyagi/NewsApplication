package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
    private var currentPageNo: Int = 1
    private var currentListSize: Int = 1
    private var totalSize: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initVariables()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun initVariables() {
        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
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
            homeViewModel.getTopHeadlines("", currentPageNo)
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
                        totalSize = resp.totalResults
                        currentListSize += list.size
                        val previousSize = homeAdapter.getCurrentListSize()
                        homeAdapter.initNewsList(list)

                        if (list.isEmpty()) {
                            hasLoadedAllItems()
                        }

                        if (isFirstLoad) {
                            isFirstLoad = false
                            homeAdapter.notifyDataSetChanged()
                        } else {
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
        if (currentListSize != 0 || currentListSize > totalSize)
            homeViewModel.getTopHeadlines("", ++currentPageNo)
    }

    override fun onNewsClicked(article: Article) {
        startActivity(NewsDetailActivity.newIntent(this, gson.toJson(article)))
    }
}
