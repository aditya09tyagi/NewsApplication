package com.example.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.squareup.picasso.Picasso

class HomeAdapter(private val picasso: Picasso): RecyclerView.Adapter<HomeViewHolder>(),
    HomeViewHolder.OnNewsClickListener {

    private lateinit var newsList: ArrayList<Article>
    private lateinit var onNewsClickListener: OnNewsClickListener


    fun initNewsList(newsList: ArrayList<Article>){
        this.newsList = newsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_news, parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (::newsList.isInitialized)
            return newsList.size
        return 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        if (::newsList.isInitialized){
            holder.setNews(newsList[position],picasso)
            holder.setOnNewsClickListener(this)
        }
    }

    fun getCurrentListSize():Int{
        if (::newsList.isInitialized)
            return newsList.size
        return 0
    }

    override fun onNewsClicked(article: Article) {
        if (::onNewsClickListener.isInitialized)
            onNewsClickListener.onNewsClicked(article)
    }

    fun setOnNewsClickListener(onNewsClickListener: OnNewsClickListener) {
        this.onNewsClickListener = onNewsClickListener
    }

    interface OnNewsClickListener {
        fun onNewsClicked(article: Article)
    }
}