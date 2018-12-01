package com.lelloman.pdfscores.recentscores

import com.lelloman.common.view.BaseActivity
import com.lelloman.pdfscores.R
import com.lelloman.pdfscores.databinding.ActivityRecentScoresBinding

class RecentScoresActivity : BaseActivity<RecentScoresViewModel, ActivityRecentScoresBinding>() {

    override val layoutResId = R.layout.activity_recent_scores

    override fun setViewModel(binding: ActivityRecentScoresBinding, viewModel: RecentScoresViewModel) {
        binding.viewModel = viewModel
    }

    override fun getViewModelClass() = RecentScoresViewModel::class.java
}
