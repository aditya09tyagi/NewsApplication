package com.example.newsapp

import android.app.Activity
import android.app.Service
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.newsapp.data.event.NoInternetEvent
import com.example.newsapp.di.components.DaggerNewsApplicationComponent
import com.example.newsapp.di.components.NewsApplicationComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import es.dmoral.toasty.Toasty
import io.github.inflationx.viewpump.ViewPump
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class NewsApp : DaggerApplication(), LifecycleObserver, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var component: NewsApplicationComponent

    private lateinit var toast: Toast

    companion object {
        const val defaultOffset = 0
        var isAppInForeground = false

        fun get(activity: Activity): NewsApp {
            return activity.application as NewsApp
        }

        fun get(service: Service): NewsApp {
            return service.application as NewsApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        addProcessObserver()
        initComponent()
        initAndroidThreeTen()
        initTimber()
        initViewPump()
        listenNoInternetEvent()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        if (!::component.isInitialized)
            initComponent()
        return component
    }

    private fun addProcessObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun initAndroidThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initTimber() {
        Timber.plant(component.timberTree())
    }

    private fun initViewPump() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(component.calligraphyInterceptor())
                .build()
        )
    }

    private fun initComponent() {
        component = DaggerNewsApplicationComponent.factory().create(applicationContext)
        component.inject(this)
    }

    private fun listenNoInternetEvent() {
        EventBus.getDefault().register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppGoBackground() {
        isAppInForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppComeForeground() {
        isAppInForeground = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNoInternetEvent(noInternetEvent: NoInternetEvent) {
        showToast()
    }

    private fun showToast() {
        if (!::toast.isInitialized) {
            toast = Toasty.error(this, getString(R.string.no_internet), Toasty.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, defaultOffset, defaultOffset)
        }
        toast.show()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}