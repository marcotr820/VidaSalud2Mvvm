package com.example.vidasalud2.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.example.vidasalud2.R

class ProgressLoading(val myActivity: Activity) {

    private var progressDialog: Dialog? = null

    fun mostrarDialog(isLoading: Boolean) {
        if (isLoading) {
            progressDialog = Dialog(myActivity)
            progressDialog?.let {
                it.show()
                it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                it.setContentView(R.layout.my_model_loading)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
            }
        } else {
            progressDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
                progressDialog = null
            }
        }

    }

//    fun ocultarDialog() {
//        progressDialog?.let {
//            if (it.isShowing) {
//                it.dismiss()
//            }
//            progressDialog = null
//        }
//    }

}