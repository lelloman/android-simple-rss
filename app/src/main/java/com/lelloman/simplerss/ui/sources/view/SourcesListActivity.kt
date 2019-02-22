package com.lelloman.simplerss.ui.sources.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.utils.ItemSwipeListener
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivitySourcesListBinding
import com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class SourcesListActivity
    : BaseActivity<SourcesListViewModel, ActivitySourcesListBinding>() {

    private lateinit var adapter: SourcesAdapter

    private val logger by lazy { loggerFactory.getLogger(SourcesListActivity::class.java) }

    private var timerSubscription: Disposable? = null

    override val layoutResId = R.layout.activity_sources_list

    override fun getViewModelClass() = SourcesListViewModel::class.java

    override fun setViewModel(binding: ActivitySourcesListBinding, viewModel: SourcesListViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        adapter = SourcesAdapter(
            resourceProvider = resourceProvider,
            semanticTimeProvider = semanticTimeProvider,
            onSourceClickedListener = viewModel::onSourceClicked,
            onSourceIsActiveChangedListener = viewModel::onSourceIsActiveChanged
        )

        binding.sourcesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.sourcesRecyclerView.adapter = adapter
        ItemSwipeListener.set(binding.sourcesRecyclerView) { viewModel.onSourceSwiped(adapter.getItem(it)) }
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
        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, SourcesListActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }

}
