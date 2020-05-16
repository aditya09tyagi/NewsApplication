package com.example.newsapp.ui.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jaeger.library.StatusBarUtil
import dagger.android.support.DaggerAppCompatActivity
import es.dmoral.toasty.Toasty
import javax.inject.Inject

open class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var gson: Gson

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigation_bar)

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.colorPrimaryDark))

    }

    protected fun showErrorToast(message: String, view: View? = null, actionText: String = getString(R.string.ok), duration: Int = Toasty.LENGTH_LONG) {

        if (NotificationManagerCompat.from(this).areNotificationsEnabled())
            Toasty.error(this, message, duration).show()
        else if (view != null) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(actionText) {
                snackbar.dismiss()
            }
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.text_red))
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.show()
        }

    }

    protected fun showSuccessToast(message: String, view: View? = null, duration: Int = Toasty.LENGTH_LONG) {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled())
            Toasty.success(this, message, duration).show()
        else if (view != null) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(getString(R.string.ok)) {
                snackbar.dismiss()
            }
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.feedback_positive))
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.show()
        }
    }
}