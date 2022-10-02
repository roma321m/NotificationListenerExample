package com.roman.notificationlistenerexample.ui.view_model

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.roman.notificationlistenerexample.util.Constants

class ViewModel {
    var text by mutableStateOf("No Notifications To Display")
        private set

    fun updateText(newText: String) {
        text = newText
    }

    fun clearAllNotifications(context: Context) {
        val intent = Intent(Constants.INTENT_FILTER_ACTION)
        intent.putExtra("command", "clearall")
        context.sendBroadcast(intent)
    }

    fun listNotifications(context: Context) {
        val intent = Intent(Constants.INTENT_FILTER_ACTION)
        intent.putExtra("command", "list")
        context.sendBroadcast(intent)
    }
}