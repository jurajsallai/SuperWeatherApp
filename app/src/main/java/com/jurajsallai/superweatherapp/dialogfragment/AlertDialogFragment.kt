package com.jurajsallai.superweatherapp.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.jurajsallai.superweatherapp.R

/**
 * created by juraj.sallai on 28.10.2018
 */
class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.error_message))
                .setPositiveButton(getString(R.string.error_button), null)
                .create()
    }
}