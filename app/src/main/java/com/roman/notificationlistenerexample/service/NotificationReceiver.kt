package com.roman.notificationlistenerexample.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.roman.notificationlistenerexample.ui.view_model.ViewModel

class NotificationReceiver(
    private val viewModel: ViewModel
): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val newText = intent?.getStringExtra("notification_event") ?: ""
        Log.d("test", "newText: $newText")
        viewModel.updateText("$newText \n ${viewModel.text}")
    }

}