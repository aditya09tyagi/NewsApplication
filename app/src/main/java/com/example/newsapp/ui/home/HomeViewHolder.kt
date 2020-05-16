package com.example.newsapp.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_news.view.*

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onNewsClickListener: OnNewsClickListener

    fun setNews(article: Article, picasso: Picasso) {
        itemView.tvNewsTime.text =
            DateTimeUtils.getDateInTimeOnlyFormatFromUtcDateTime(article.publishedAt)
        itemView.tvNewsHeading.text = article.title
        picasso.load(article.urlToImage)
            .error(R.drawable.placeholder_no_internet)
            .placeholder(R.drawable.progress_animation)
            .into(itemView.ivNewsImage)

        itemView.tvNewsSource.text = article.source.name

        itemView.setOnClickListener {
            if (::onNewsClickListener.isInitialized)
                onNewsClickListener.onNewsClicked(article)
        }
    }

    fun setOnNewsClickListener(onNewsClickListener: OnNewsClickListener) {
        this.onNewsClickListener = onNewsClickListener
    }

    interface OnNewsClickListener {
        fun onNewsClicked(article: Article)
    }
}