package com.example.newsapp.di.modules.helper

import android.content.Context
import com.example.newsapp.di.modules.libraries.PicassoModule
import com.example.newsapp.di.scopes.NewsApplicationScope
import com.example.newsapp.util.*
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, PicassoModule::class])
class UtilsModule {

    @Provides
    @NewsApplicationScope
    fun networkUtil(context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

}