package com.lelloman.simplerss.ui.articles.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lelloman.common.logger.Logger
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityArticleBinding
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_URL

class ArticleActivity : BaseActivity<com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel, ActivityArticleBinding>() {

    override val layoutResId = R.layout.activity_article

    override fun getViewModelClass() = com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel::class.java

    private lateinit var logger: Logger

    override fun setViewModel(binding: ActivityArticleBinding, viewModel: com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel) = Unit

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger = loggerFactory.getLogger(com.lelloman.simplerss.ui.articles.view.ArticleActivity::class.java)

        val url = intent.getStringExtra(ARG_URL)
        if (url == null) {
            logger.e("$ARG_URL was not set in the Intent.")
            finish()
        } else {
            binding.webView.let { webView ->
                webView.webChromeClient = WebChromeClient()
                webView.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        binding.progressBar.visibility = View.GONE
                    }
                }
                with(webView.settings) {
                    javaScriptEnabled = true
                }
                webView.loadUrl(url)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, com.lelloman.simplerss.ui.articles.view.ArticleActivity::class.java)
                    .putExtra(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARG_URL, deepLink.getString(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARG_URL))
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}