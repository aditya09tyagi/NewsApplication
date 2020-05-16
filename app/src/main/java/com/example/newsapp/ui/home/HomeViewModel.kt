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

    fun getTopHeadlines(query: String, pageNo: Int) {
        _newsLiveData.postValue(Resource.loading())
        newsAppRepository.getTopHeadlines(query, pageNo, this)
    }

    override fun onGetTopHeadlinesSuccess(newsResponse: NewsResponse) {
        _newsLiveData.postValue(Resource.success(newsResponse))
    }

    override fun onGetTopHeadlinesFailure(error: Error) {
        _newsLiveData.postValue(Resource.error(error))
    }
}