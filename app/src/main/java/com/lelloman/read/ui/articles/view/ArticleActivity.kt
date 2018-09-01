package com.lelloman.read.ui.articles.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityArticleBinding
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModel

class ArticleActivity : BaseActivity<ArticleViewModel, ActivityArticleBinding>() {

    override val layoutResId = R.layout.activity_article

    override fun getViewModelClass() = ArticleViewModel::class.java

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val article = intent.getParcelableExtra<SourceArticle>(ARG_ARTICLE)
        if (article == null) {
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
                webView.loadUrl(article.link)
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

        private const val ARG_ARTICLE = "Article"

        fun start(activity: Activity, article: SourceArticle) {
            val intent = Intent(activity, ArticleActivity::class.java)
                .putExtra(ARG_ARTICLE, article)
            activity.startActivity(intent)
        }
    }
}