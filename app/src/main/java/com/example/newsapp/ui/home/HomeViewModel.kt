package com.example.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.data.models.Error
import com.example.newsapp.data.models.Resource
import com.example.newsapp.data.models.NewsResponse
import com.example.newsapp.data.network.NewsAppRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val newsAppRepository: NewsAppRepository
) : ViewModel(), NewsAppRepository.OnGetTopHeadlinesListener {

    private val _newsLiveData = MutableLiveData<Resource<NewsResponse>>()
    val newsLiveData: LiveData<Resource<NewsResponse>>
        get() = _newsLiveData

    private var pageNumber = 1

    fun getTopHeadlines(query: String, sourceName: String?) {
        _newsLiveData.postValue(Resource.loading())
        newsAppRepository.getTopHeadlines(query, pageNumber, sourceName = sourceName,onGetTopHeadlinesListener = this)
    }

    fun refresh() {
        pageNumber = 1
    }

    override fun onGetTopHeadlinesSuccess(newsResponse: NewsResponse) {
        _newsLiveData.postValue(Resource.success(newsResponse))
        pageNumber++
    }

    override fun onGetTopHeadlinesFailure(error: Error) {
        _newsLiveData.postValue(Resource.error(error))
    }
}