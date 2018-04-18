package com.hlabexamples.easyapi.kotlin.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast

import com.hlabexmaples.easyapi.R

/**
 * Purpose of this Class is to display different dialog in application.
 */
object DialogUtils {

    /**
     * Displays alert dialog to show common messages.
     *
     * @param message Message to be shown in the Dialog displayed
     * @param context Context of the Application, where the Dialog needs to be displayed
     */
    fun displayDialog(context: Context, message: String) {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setCancelable(false)

        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }

        if (!(context as Activity).isFinishing) {
            alertDialog.show()
        }
    }

    /**
     * Displays alert dialog to show common messages.
     *
     * @param message Message to be shown in the Dialog displayed
     * @param context Context of the Application, where the Dialog needs to be displayed
     */
    fun displayDialogSettings(context: Context, message: String) {

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setCancelable(false)

        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel)) { dialog, _ ->
            context.startActivity(Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS))
            dialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.settings)) { dialog, _ -> dialog.dismiss() }

        if (!(context as Activity).isFinishing) {
            alertDialog.show()
        }
    }

    /**
     * Displays toast message
     *
     * @param mContext requires to create Toast
     */
    fun showToast(mContext: Context, message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}
