package com.example.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.squareup.picasso.Picasso

class HomeAdapter(private val picasso: Picasso) : RecyclerView.Adapter<HomeViewHolder>(),
    HomeViewHolder.OnNewsClickListener {

    private var newsList: ArrayList<Article> = ArrayList()
    private lateinit var onNewsClickListener: OnNewsClickListener

    fun addNewItems(newsList: ArrayList<Article>) {
        this.newsList.addAll(newsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_news, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (newsList.isNotEmpty())
            return newsList.size
        return 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        if (newsList.isNotEmpty()) {
            holder.setNews(newsList[position], picasso)
            holder.setOnNewsClickListener(this)
        }
    }

    fun getCurrentListSize(): Int {
        return newsList.size
    }

    fun clearList() {
        newsList.clear()
    }

    override fun onNewsClicked(
        newsImageView: ImageView,
        titleTextView: TextView,
        article: Article
    ) {
        if (::onNewsClickListener.isInitialized)
            onNewsClickListener.onNewsClicked(newsImageView, titleTextView, article)
    }

    fun setOnNewsClickListener(onNewsClickListener: OnNewsClickListener) {
        this.onNewsClickListener = onNewsClickListener
    }

    interface OnNewsClickListener {
        fun onNewsClicked(newsImageView: ImageView, titleTextView: TextView, article: Article)
    }
}