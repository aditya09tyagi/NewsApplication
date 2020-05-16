package com.example.newsapp.ui.news_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.ui.base.BaseActivity

class NewsDetailActivity : BaseActivity() {

    private lateinit var article: Article
    companion object {
        private const val EXTRA_ARTICLE_DATA = "EXTRA_ARTICLE_DATA"
        fun newIntent(context: Context, article: String): Intent {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE_DATA, article)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        getArguments()
        initialiseLayout()
        setListener()
    }

    private fun getArguments(){
        intent?.let {
            it.getStringExtra(EXTRA_ARTICLE_DATA)?.let {artcle->
                article = gson.fromJson(artcle,Article::class.java)
            }
        }
    }

    private fun initialiseLayout(){

    }

    private fun setListener(){

    }
}