package com.animsh.quickpay.utils

import android.app.Activity
import android.app.AlertDialog
import com.animsh.quickpay.R

/**
 * Created by animsh on 4/14/2021.
 */
class LoadingDialog(
    private val activity: Activity,
) {

    private lateinit var alertDialog: AlertDialog

    fun showLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.layout_loading_dialog, null))
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismissDialog() = alertDialog.dismiss()

}