package com.lelloman.simplerss.ui.debug.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.lelloman.simplerss.R

@Deprecated(message = "Move this to a generic confirmation dialog fragment in common")
class ResetSharedPrefsConfirmationDialogFragment : DialogFragment() {

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
            .setTitle(R.string.reset_shared_prefs_confirmation_title)
            .setMessage(R.string.reset_shared_prefs_confirmation_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                listener?.onResetSharedPrefsConfirmed()
            }
            .create()
    }

    interface Listener {
        fun onResetSharedPrefsConfirmed()
    }
}