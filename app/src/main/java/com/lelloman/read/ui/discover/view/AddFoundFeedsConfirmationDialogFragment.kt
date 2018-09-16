package com.lelloman.read.ui.discover.view

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.read.R
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.navigation.ReadNavigationScreen.Companion.ARG_FOUND_FEEDS

class AddFoundFeedsConfirmationDialogFragment : DialogFragment() {

    private var listener: Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val foundFeeds: ArrayList<FoundFeed> = arguments?.getParcelableArrayList(ARG_FOUND_FEEDS)
            ?: ArrayList()
        val message = if (foundFeeds.isNotEmpty()) {
            getString(
                R.string.add_all_sources_message,
                foundFeeds.size,
                foundFeeds.joinToString("\n") { "- ${it.name}" }
            )
        } else {
            getString(R.string.add_all_sources_empty_list)
        }

        return AlertDialog
            .Builder(activity)
            .setTitle(R.string.add_sources)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                listener?.onAddAllFoundFeedsConfirmationClicked(foundFeeds)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    companion object {

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                if (context is Activity) {
                    val fragment = AddFoundFeedsConfirmationDialogFragment()
                    fragment.arguments = Bundle().apply {
                        val foundFeeds: ArrayList<FoundFeed> = deepLink.getSerializableArrayList(ARG_FOUND_FEEDS)!!
                        putParcelableArrayList(ARG_FOUND_FEEDS, foundFeeds)
                    }
                    fragment.show(context.fragmentManager, AddFoundFeedsConfirmationDialogFragment::class.java.simpleName)
                } else {
                    throw IllegalArgumentException("Context argument must be an Activity")
                }
            }
        }
    }

    interface Listener {
        fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<FoundFeed>)
    }
}