package com.lelloman.simplerss.ui.debug.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.simplerss.R

@Deprecated(message = "Move this to a generic confirmation dialog fragment in common")
class ResetDbConfirmationDialogFragment : DialogFragment() {

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
        return AlertDialog
            .Builder(activity)
            .setTitle(R.string.reset_db_confirmation_title)
            .setMessage(R.string.reset_db_confirmation_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                listener?.onResetDbConfirmed()
            }
            .create()
    }

    companion object {
        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                if (context is FragmentActivity) {
                    val fragment = ResetDbConfirmationDialogFragment()
                    fragment.show(context.supportFragmentManager, ResetDbConfirmationDialogFragment::class.java.simpleName)
                } else {
                    throw IllegalArgumentException("Context argument must be a FragmentActivity")
                }
            }
        }
    }

    interface Listener {
        fun onResetDbConfirmed()
    }
}