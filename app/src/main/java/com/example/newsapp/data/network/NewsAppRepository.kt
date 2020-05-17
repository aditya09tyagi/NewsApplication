package com.example.newsapp.data.network

import com.example.newsapp.data.models.Error
import com.example.newsapp.data.models.NewsResponse
import com.example.newsapp.util.ApiCallback

class NewsAppRepository(private val newsAppService: NewsAppService) {

    fun getTopHeadlines(
        query: String,
        pageNo: Int,
        sourceName: String?,
        onGetTopHeadlinesListener: OnGetTopHeadlinesListener
    ) {
        newsAppService.getTopHeadlines(searchQuery = query, pageNo = pageNo,sourceName = sourceName)
            .enqueue(object : ApiCallback<NewsResponse>() {
                override fun success(response: NewsResponse) {
                    onGetTopHeadlinesListener.onGetTopHeadlinesSuccess(response)
                }

                override fun failure(error: Error) {
                    onGetTopHeadlinesListener.onGetTopHeadlinesFailure(error)
                }

            })
    }

    //-------- INTERFACES-------

    interface OnGetTopHeadlinesListener {

        fun onGetTopHeadlinesSuccess(newsResponse: NewsResponse)

        fun onGetTopHeadlinesFailure(error: Error)

    }
}