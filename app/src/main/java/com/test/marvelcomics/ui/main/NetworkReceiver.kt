package com.test.marvelcomics.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo
        if (networkInfo != null) {
            Toast.makeText(context, "Есть интернет соединение", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Нет интернета", Toast.LENGTH_SHORT).show()
        }
    }
}