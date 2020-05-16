package com.example.newsapp.di.modules.activity

import com.example.newsapp.di.modules.fragment.BaseFragmentModule
import com.example.newsapp.di.scopes.PerActivityScope
import com.example.newsapp.ui.home.HomeActivity
import com.example.newsapp.ui.news_detail.NewsDetailActivity
import com.example.newsapp.ui.news_detail.detail_web_view.NewsDetailWebViewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [BaseFragmentModule::class])
abstract class ActivityBindingModule{

    //Add module of the activity if the activity is dependent on any constraint here
    @PerActivityScope
    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    internal abstract fun homeActivity():HomeActivity

    @PerActivityScope
    @ContributesAndroidInjector
    internal abstract fun newsDetailActivity():NewsDetailActivity

    @PerActivityScope
    @ContributesAndroidInjector
    internal abstract fun newsDetailWebViewActivity():NewsDetailWebViewActivity

}