package com.example.newsapp.ui.news_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.ui.base.BaseActivity
import com.example.newsapp.ui.gestures.OnSwipeTouchListener
import com.example.newsapp.ui.news_detail.detail_web_view.NewsDetailWebViewActivity
import com.example.newsapp.ui.widget.photo_view_layout.StfalconImageViewer
import com.jaeger.library.StatusBarUtil
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_details.*
import javax.inject.Inject

class NewsDetailActivity : BaseActivity() {

    @Inject
    lateinit var picasso: Picasso

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
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.setLightMode(this)
        getArguments()
        initialiseLayout()
        setListener()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_ARTICLE_DATA)?.let { artcle ->
                article = gson.fromJson(artcle, Article::class.java)
            }
        }
    }

    private fun initialiseLayout() {
        if (::article.isInitialized) {
            picasso.load(article.urlToImage)
                .error(R.drawable.placeholder_no_internet)
                .placeholder(R.drawable.progress_animation)
                .into(ivNews)

            tvTitle.text = article.title

            tvDescription.text = article.content

            tvSource.text = article.source.name

            tvAuthor.text = article.author
        }
    }

    private fun setListener() {

        ivNews.setOnClickListener {
            if (::article.isInitialized)
                openPhotoView(article.urlToImage,ivNews)
        }

        tvSwipeLeftForMore.setOnClickListener {
            openDetailWebViewActivity()
        }

        clDetailsContainer.setOnTouchListener(object :
            OnSwipeTouchListener(this@NewsDetailActivity) {
            override fun onSwipeLeft() {
                openDetailWebViewActivity()
            }

            override fun onSwipeRight() {}
            override fun onSwipeTop() {}
            override fun onSwipeBottom() {}
        })
    }

    private fun openPhotoView(url: String, imageView: ImageView) {
        val images = listOf(url)
        val photoView = getPhotoView(this, images, imageView)
        photoView.show(true)
    }

    private fun getPhotoView(
        context: Context,
        images: List<String>,
        imageView: ImageView
    ): StfalconImageViewer<String> {
        return StfalconImageViewer.Builder<String>(context, images, ::loadPosterImage)
            .withStartPosition(0)
            .withBackgroundColorResource(R.color.black)
            .withHiddenStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .withTransitionFrom(imageView)
            .build()
    }

    private fun loadPosterImage(imageView: ImageView, imageUrl: String) {
        picasso.load(imageUrl).into(imageView)
    }

    private fun openDetailWebViewActivity() {
        if (::article.isInitialized) {
            startActivity(
                NewsDetailWebViewActivity.newIntent(
                    this@NewsDetailActivity,
                    article.url,
                    article.title
                )
            )
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }
    }
}