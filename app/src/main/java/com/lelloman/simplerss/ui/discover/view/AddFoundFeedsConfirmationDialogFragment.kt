package com.lelloman.simplerss.ui.discover.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.lelloman.simplerss.R
import com.lelloman.simplerss.feed.finder.FoundFeed

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
                foundFeeds.size.toString(),
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

        private const val ARG_FOUND_FEEDS = "FoundFeeds"

        fun newInstance(foundFeeds: ArrayList<FoundFeed>): AddFoundFeedsConfirmationDialogFragment {
            val fragment = AddFoundFeedsConfirmationDialogFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_FOUND_FEEDS, foundFeeds)
            }
            return fragment
        }
    }

    interface Listener {
        fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<FoundFeed>)
    }
}