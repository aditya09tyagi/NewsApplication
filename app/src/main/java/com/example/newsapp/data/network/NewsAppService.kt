package com.example.newsapp.data.network

import com.example.newsapp.data.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAppService {

    companion object {
        private const val API_KEY = "310f3540460c4e808d5d9cd649118852"
    }

    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("q") searchQuery: String = "",
        @Query("page") pageNo: Int,
        @Query("country") country: String = "us"
    ): Call<NewsResponse>
}