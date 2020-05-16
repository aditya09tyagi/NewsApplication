package com.example.newsapp.ui.loader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import ru.alexbykov.nopaginate.item.LoadingItem

class CustomLoadingListItemCreator(private val isLightTheme: Boolean) : LoadingItem {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_loading_list_item, parent, false)
        return LoadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, p1: Int) {
        (holder as LoadingViewHolder).let {
            it.setUI(isLightTheme)
            it.setLoadingText(holder.itemView.context.getString(R.string.loading_more))
        }
    }
}