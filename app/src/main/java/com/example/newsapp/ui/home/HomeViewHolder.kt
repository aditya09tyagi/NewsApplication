package com.example.newsapp.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_news.view.*
import org.threeten.bp.format.TextStyle
import java.util.*

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onNewsClickListener: OnNewsClickListener

    fun setNews(article: Article, picasso: Picasso) {
        itemView.tvNewsTime.text =
            getDateFromString(article.publishedAt)
        itemView.tvNewsHeading.text = article.title
        picasso.load(article.urlToImage)
            .error(R.drawable.placeholder_no_internet)
            .placeholder(R.drawable.progress_animation)
            .into(itemView.ivNewsImage)

        itemView.tvNewsSource.text = article.source.name

        itemView.setOnClickListener {
            if (::onNewsClickListener.isInitialized)
                onNewsClickListener.onNewsClicked(itemView.ivNewsImage,itemView.tvNewsHeading,article)
        }
    }

    fun setOnNewsClickListener(onNewsClickListener: OnNewsClickListener) {
        this.onNewsClickListener = onNewsClickListener
    }

    interface OnNewsClickListener {
        fun onNewsClicked(newsImageView: ImageView, titleTextView: TextView, article: Article)
    }

    private fun getDateFromString(startDate: String): String {
        val date = DateTimeUtils.getLocalDateFromString(startDate)
        val time = DateTimeUtils.getDateInTimeOnlyFormatFromUtcDateTime(startDate)
        return String.format(
            itemView.context.getString(R.string.class_date_format),
            time,
            date.dayOfMonth,
            date.month.getDisplayName(TextStyle.SHORT, Locale.UK)
        )
    }
}