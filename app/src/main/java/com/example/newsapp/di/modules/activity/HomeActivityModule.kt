package com.example.newsapp.di.modules.activity

import androidx.lifecycle.ViewModel
import com.example.newsapp.di.mapkey.ViewModelKey
import com.example.newsapp.di.scopes.PerActivityScope
import com.example.newsapp.ui.home.HomeAdapter
import com.example.newsapp.ui.home.HomeViewModel
import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

//Add whatever is needed in home activity either view model or the adapter or any fragment module if required ,etc here

@Module
abstract class HomeActivityModule {

    companion object{
        @Provides
        @PerActivityScope
        fun setAdapter(picasso: Picasso):HomeAdapter{
            return HomeAdapter(picasso)
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}