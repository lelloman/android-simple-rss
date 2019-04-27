package com.lelloman.simplerss.ui.articles.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lelloman.common.logger.Logger
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityArticleBinding
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_URL
import com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel

class ArticleActivity : BaseActivity<ArticleViewModel, ActivityArticleBinding>() {

    override val layoutResId = R.layout.activity_article

    override fun getViewModelClass() = ArticleViewModel::class.java

    private lateinit var logger: Logger

    override fun setViewModel(binding: ActivityArticleBinding, viewModel: ArticleViewModel) {
        binding.viewModel = viewModel
        logger = loggerFactory.getLogger(ArticleActivity::class.java)

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

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, ArticleActivity::class.java)
                    .putExtra(ARG_URL, deepLink.getString(ARG_URL))
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}