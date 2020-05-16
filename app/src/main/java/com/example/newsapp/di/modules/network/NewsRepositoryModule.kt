package com.example.newsapp.di.modules.network

import com.example.newsapp.data.network.NewsAppRepository
import com.example.newsapp.data.network.NewsAppService
import com.example.newsapp.di.scopes.NewsApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [NewsServiceModule::class])
class NewsRepositoryModule {

    @Provides
    @NewsApplicationScope
    fun newsAppRepository(newsAppService: NewsAppService): NewsAppRepository {
        return NewsAppRepository(newsAppService)
    }
}