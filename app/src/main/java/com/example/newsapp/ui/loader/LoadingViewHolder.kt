package com.example.newsapp.ui.loader

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import kotlinx.android.synthetic.main.custom_loading_list_item.view.*


class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setUI(isLightTheme: Boolean) {
        if (isLightTheme) {
            itemView.tv_loading_text.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark))
            itemView.progressBar.indeterminateTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorPrimaryDark)
        } else {
            itemView.tv_loading_text.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            itemView.progressBar.indeterminateTintList = ContextCompat.getColorStateList(itemView.context, R.color.white)
        }
    }

    fun setLoadingText(text: String) {
        itemView.tv_loading_text.text = text
    }
}