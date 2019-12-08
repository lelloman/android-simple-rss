package com.lelloman.simplerss.ui.debug.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityDebugBinding
import com.lelloman.simplerss.ui.debug.viewmodel.DebugViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DebugActivity :
    BaseActivity<DebugViewModel, ActivityDebugBinding>(),
    ResetDbConfirmationDialogFragment.Listener,
    ResetSharedPrefsConfirmationDialogFragment.Listener {

    override val layoutResId = R.layout.activity_debug

    override val viewModel by viewModel<DebugViewModel>()

    override fun setViewModel(binding: ActivityDebugBinding, viewModel: DebugViewModel) {
        binding.viewModel = viewModel
    }

    override fun onResetDbConfirmed() = viewModel.onResetDbConfirmed()

    override fun onResetSharedPrefsConfirmed() = viewModel.onResetSharedPrefsConfirmed()

    companion object {

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, DebugActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}