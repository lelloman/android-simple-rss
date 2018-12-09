package com.lelloman.pdfscores.ui.collectionapps

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.pdfscores.R
import com.lelloman.pdfscores.databinding.ActivityCollectionAppsBinding

class CollectionAppsActivity : BaseActivity<CollectionAppsViewModel, ActivityCollectionAppsBinding>() {

    override val layoutResId = R.layout.activity_collection_apps

    override fun setViewModel(binding: ActivityCollectionAppsBinding, viewModel: CollectionAppsViewModel) {
        binding.viewModel = viewModel
    }

    override fun getViewModelClass() = CollectionAppsViewModel::class.java

    companion object {
        val deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, CollectionAppsActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }
}