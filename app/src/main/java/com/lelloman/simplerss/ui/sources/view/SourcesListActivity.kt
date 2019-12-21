package com.lelloman.simplerss.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelloman.common.utils.ItemSwipeListener
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivitySourcesListBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class SourcesListActivity
    : SimpleRssActivity<SourcesListViewModel, ActivitySourcesListBinding>() {

    private lateinit var adapter: SourcesAdapter

    private var timerSubscription: Disposable? = null

    override val layoutResId = R.layout.activity_sources_list

    override val viewModel by viewModel<SourcesListViewModel>()

    override fun setViewModel(
        binding: ActivitySourcesListBinding,
        viewModel: SourcesListViewModel
    ) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = SourcesAdapter(
            onSourceClickedListener = viewModel::onSourceClicked,
            onSourceIsActiveChangedListener = viewModel::onSourceIsActiveChanged
        )

        binding.sourcesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.sourcesRecyclerView.adapter = adapter
        ItemSwipeListener.set(binding.sourcesRecyclerView) {
            viewModel.onSourceSwiped(
                adapter.getItem(
                    it
                )
            )
        }
        viewModel.sources.observe(this, adapter)
    }

    override fun onStart() {
        super.onStart()
        timerSubscription = Observable
            .interval(1, TimeUnit.SECONDS)
            .observeOn(uiScheduler)
            .subscribe {
                adapter.tick()
                logger.d("1s tick")
            }
    }

    override fun onStop() {
        super.onStop()
        timerSubscription?.dispose()
        timerSubscription = null
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SourcesListActivity::class.java))
        }
    }
}
