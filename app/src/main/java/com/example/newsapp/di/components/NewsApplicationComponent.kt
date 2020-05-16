package com.example.newsapp.di.components

import android.content.Context
import com.example.newsapp.NewsApp
import com.example.newsapp.data.network.NewsAppRepository
import com.example.newsapp.di.modules.activity.ActivityBindingModule
import com.example.newsapp.di.modules.application.NewsAppModule
import com.example.newsapp.di.modules.helper.UtilsModule
import com.example.newsapp.di.modules.helper.ViewModelFactoryModule
import com.example.newsapp.di.modules.libraries.PicassoModule
import com.example.newsapp.di.modules.network.NewsRepositoryModule
import com.example.newsapp.di.scopes.NewsApplicationScope
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@NewsApplicationScope
@Component(
    modules = [
        NewsAppModule::class,
        NewsRepositoryModule::class,
        PicassoModule::class,
        UtilsModule::class,
        ViewModelFactoryModule::class,
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface NewsApplicationComponent : AndroidInjector<NewsApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): NewsApplicationComponent
    }

    fun calligraphyInterceptor(): CalligraphyInterceptor

    fun timberTree(): Timber.Tree

    fun getPicasso(): Picasso

    fun getNewsAppRepository(): NewsAppRepository

    fun getGson(): Gson

}