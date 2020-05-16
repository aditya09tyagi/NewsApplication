package com.example.newsapp.ui.news_detail.detail_web_view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.ui.base.BaseActivity
import com.example.newsapp.ui.gestures.OnSwipeTouchListener
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_full_web_view.*

class NewsDetailWebViewActivity : BaseActivity() {
    private var isError = false
    private lateinit var htmlLink: String
    private lateinit var newsTitle: String

    companion object {
        private const val EXTRA_HTML_LINK = "EXTRA_HTML_LINK"
        private const val EXTRA_NEWS_TITLE = "EXTRA_NEWS_TITLE"
        fun newIntent(context: Context, htmlString: String, newsTitle: String): Intent {
            val intent = Intent(context, NewsDetailWebViewActivity::class.java)
            intent.putExtra(EXTRA_HTML_LINK, htmlString)
            intent.putExtra(EXTRA_NEWS_TITLE, newsTitle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_web_view)

        setup()
    }

    private fun setup() {
        setWindowInsets()
        getArguments()
        setStatusBarColor()
        initialiseLayout()
        setClickListeners()
        renderLink()
    }

    private fun getArguments() {
        intent.extras?.let {
            it.getString(EXTRA_HTML_LINK)?.let { link ->
                htmlLink = link
            }
            it.getString(EXTRA_NEWS_TITLE)?.let { title ->
                newsTitle = title
            }
        }
    }

    private fun setWindowInsets() {
        parentView.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun initialiseLayout() {
        if (::newsTitle.isInitialized)
            tvHeading.text = newsTitle
    }

    private fun setClickListeners() {
        retryButton.setOnClickListener {
            retryLoading()
        }
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        fullWebView.setOnTouchListener(object :
            OnSwipeTouchListener(this@NewsDetailWebViewActivity) {
            override fun onSwipeRight() {
                onBackPressed()
            }
            override fun onSwipeLeft() {}
            override fun onSwipeTop() {}
            override fun onSwipeBottom() {}
        })
    }

    private fun setStatusBarColor() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.setLightMode(this)
    }

    private fun renderLink() {
        fullWebView.settings.javaScriptEnabled = true
        fullWebView.settings.loadWithOverviewMode = true
        fullWebView.settings.useWideViewPort = true
        fullWebView.loadUrl(htmlLink)
        fullWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (!isError)
                    showWebView()
                else
                    showErrorView()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                isError = true
            }
        }
    }

    private fun showLoadingView() {
        errorHolder.visibility = View.GONE
        fullWebView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    private fun showWebView() {
        loadingView.visibility = View.GONE
        errorHolder.visibility = View.GONE
        fullWebView.visibility = View.VISIBLE
    }

    private fun showErrorView() {
        loadingView.visibility = View.GONE
        fullWebView.visibility = View.GONE
        errorHolder.visibility = View.VISIBLE
    }

    private fun retryLoading() {
        isError = false
        showLoadingView()
        fullWebView.loadUrl(htmlLink)
    }
}