package com.example.newsapp.di.modules.application

import com.example.newsapp.R
import com.example.newsapp.di.scopes.NewsApplicationScope
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@Module
object NewsAppModule {

    @Provides
    @NewsApplicationScope
    fun timberTree(): Timber.Tree {
        return Timber.DebugTree()
    }

    @Provides
    @NewsApplicationScope
    fun calligraphyInterceptor(): CalligraphyInterceptor {
        return CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        )
    }
}