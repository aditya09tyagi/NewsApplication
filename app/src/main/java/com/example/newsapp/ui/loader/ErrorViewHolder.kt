package com.example.newsapp.ui.loader

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import kotlinx.android.synthetic.main.custom_error_list_item.view.*

class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onRetryListener: OnRetryListener

    init {
        itemView.btnRetry.setOnClickListener {
            if (::onRetryListener.isInitialized)
                onRetryListener.onRetry()
        }
    }

    fun setUI(isLightTheme: Boolean) {
        if (isLightTheme) {
            itemView.tvRetry.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_text_primary))
            itemView.tvRetry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_retry_black, 0, 0, 0)
        } else {
            itemView.tvRetry.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            itemView.tvRetry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_retry_white, 0, 0, 0)
        }
    }

    interface OnRetryListener {
        fun onRetry()
    }

    fun setOnRetryListener(onRetryListener: OnRetryListener) {
        this.onRetryListener = onRetryListener
    }
}