package com.lelloman.simplerss.ui.articles.view

import android.app.Activity
import android.content.Intent
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityArticleBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ArticleActivity : SimpleRssActivity<ArticleViewModel, ActivityArticleBinding>() {

    override val layoutResId = R.layout.activity_article

    override val viewModel by viewModel<ArticleViewModel>()

    override fun setViewModel(binding: ActivityArticleBinding, viewModel: ArticleViewModel) {
        binding.viewModel = viewModel

        val url = intent.getStringExtra(ARG_URL)
        if (url == null) {
            logger.e("'$ARG_URL' extra was not set in the Intent.")
            finish()
        } else {
            binding.webView.loadUrl(url)
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
        private const val ARG_URL = "Url"
        fun start(activity: Activity, url: String) {
            val intent = Intent(activity, ArticleActivity::class.java)
                .putExtra(ARG_URL, url)
            activity.startActivity(intent)
        }
    }
}