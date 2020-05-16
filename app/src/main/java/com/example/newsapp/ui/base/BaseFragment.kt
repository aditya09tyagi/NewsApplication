package com.example.newsapp.ui.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import es.dmoral.toasty.Toasty

abstract class BaseFragment : DaggerFragment() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    protected fun showErrorToast(message: String, view: View? = null, gravity: Int = Gravity.BOTTOM, duration: Int = Toasty.LENGTH_LONG) {
        context?.let {
            if (NotificationManagerCompat.from(it).areNotificationsEnabled()) {
                val toast = Toasty.error(it, message, duration)
                toast.setGravity(gravity, 0, 50)
                toast.show()
            } else if (view != null) {
                val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                snackbar.setAction(getString(R.string.ok)) {
                    snackbar.dismiss()
                }
                snackbar.view.setBackgroundColor(ContextCompat.getColor(it, R.color.text_red))
                snackbar.setActionTextColor(ContextCompat.getColor(it, R.color.white))
                snackbar.show()
            }
        }
    }

    protected fun showSuccessToast(message: String, view: View? = null, duration: Int = Toasty.LENGTH_LONG) {
        context?.let {
            if (NotificationManagerCompat.from(it).areNotificationsEnabled())
                Toasty.success(it, message, duration).show()
            else if (view != null) {
                val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                snackbar.setAction(getString(R.string.ok)) {
                    snackbar.dismiss()
                }
                snackbar.view.setBackgroundColor(ContextCompat.getColor(it, R.color.feedback_positive))
                snackbar.setActionTextColor(ContextCompat.getColor(it, R.color.white))
                snackbar.show()
            }
        }
    }
}