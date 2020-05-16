package com.example.newsapp.di.modules.activity

import com.example.newsapp.di.modules.fragment.BaseFragmentModule
import com.example.newsapp.di.scopes.PerActivityScope
import com.example.newsapp.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [BaseFragmentModule::class])
abstract class ActivityBindingModule{

    //Add module of the activity if the activity is dependent on any constraint here
    @PerActivityScope
    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    internal abstract fun homeActivity():HomeActivity

}