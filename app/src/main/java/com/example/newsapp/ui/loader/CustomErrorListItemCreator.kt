package com.example.newsapp.ui.loader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import ru.alexbykov.nopaginate.callback.OnRepeatListener
import ru.alexbykov.nopaginate.item.ErrorItem

class CustomErrorListItemCreator(private val isLightTheme: Boolean) : ErrorItem {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_error_list_item, parent, false)
        return ErrorViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, p1: Int, listener: OnRepeatListener?) {
        holder?.let {
            (it as ErrorViewHolder).let {
                it.setUI(isLightTheme)
                it.setOnRetryListener(object : ErrorViewHolder.OnRetryListener {
                    override fun onRetry() {
                        listener?.let {
                            it.onClickRepeat()
                        }
                    }
                })
            }
        }
    }
}