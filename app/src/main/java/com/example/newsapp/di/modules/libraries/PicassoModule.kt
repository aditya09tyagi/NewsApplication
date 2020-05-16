package com.example.newsapp.di.modules.libraries

import android.content.Context
import com.example.newsapp.di.modules.helper.ContextModule
import com.example.newsapp.di.scopes.NewsApplicationScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
object PicassoModule {

    @Provides
    @NewsApplicationScope
    fun picasso(context: Context): Picasso {
        return Picasso.Builder(context)
                .build()
    }
}