@file:Suppress("unused")

package com.example.newsapp.di.modules.network

import com.example.newsapp.data.network.NewsAppService
import com.example.newsapp.di.scopes.NewsApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [NetworkModule::class])
class NewsServiceModule {

    companion object {
        private const val BASE_URL = "https://newsapi.org/"
    }

    @Provides
    @NewsApplicationScope
    fun newsAppService(retrofit: Retrofit): NewsAppService {
        return retrofit.create(NewsAppService::class.java)
    }

    @Provides
    @NewsApplicationScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @NewsApplicationScope
    fun gson(): Gson {
        return GsonBuilder().create()
    }
}