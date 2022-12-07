package com.example.mvvmovie.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mvvmovie.R



fun isInternetAvailable(): Boolean {
    val result: Boolean
    val connectivityManager = SessionManager.context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

    return result
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.show(condition: Boolean? = true) {
    if (condition == true) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.hide(condition: Boolean? = true) {
    if (condition == true) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}

fun ImageView.setImageUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        //.placeholder(R.drawable.default_pic)
        .into(this)
}


fun Context.showAlert(
    title: String = "",
    message: String = "",
    negativeClick: () -> Unit
    ){
    if (this is Activity && !this.isFinishing) {
        val productDialog = Dialog(this, R.style.PopupDialogCustom)
        productDialog.setContentView(R.layout.layout_alert_dialog_title)
        productDialog.setCancelable(false)

        val tvTitle: TextView = productDialog.findViewById(R.id.tv_title)
        val tvMessage: TextView = productDialog.findViewById(R.id.tv_message)
        val btnRetry: Button = productDialog.findViewById(R.id.btn_retry)

        tvTitle.text = title
        tvMessage.text = message
        btnRetry.setOnClickListener {
            negativeClick.invoke()
            productDialog.dismiss()
        }

        productDialog.show()
    }

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
        val intent = Intent(this, it)
        intent.putExtras(Bundle().apply(extras))
        startActivity(intent)
    }


}



