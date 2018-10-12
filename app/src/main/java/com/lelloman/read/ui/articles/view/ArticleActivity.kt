package com.lelloman.read.ui.articles.view

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
import com.lelloman.read.R
import com.lelloman.read.databinding.ActivityArticleBinding
import com.lelloman.read.navigation.ReadNavigationScreen
import com.lelloman.read.navigation.ReadNavigationScreen.Companion.ARG_URL
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModel

class ArticleActivity : BaseActivity<ArticleViewModel, ActivityArticleBinding>() {

    override val layoutResId = R.layout.activity_article

    override fun getViewModelClass() = ArticleViewModel::class.java

    private lateinit var logger: Logger

    override fun setViewModel(binding: ActivityArticleBinding, viewModel: ArticleViewModel) = Unit

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger = loggerFactory.getLogger(ArticleActivity::class.java)

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
                val intent = Intent(context, ArticleActivity::class.java)
                    .putExtra(ReadNavigationScreen.ARG_URL, deepLink.getString(ReadNavigationScreen.ARG_URL))
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}