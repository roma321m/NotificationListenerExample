package com.roman.notificationlistenerexample.activity

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.roman.notificationlistenerexample.service.NLService
import com.roman.notificationlistenerexample.service.NotificationReceiver
import com.roman.notificationlistenerexample.service.NotificationService
import com.roman.notificationlistenerexample.ui.screens.MainScreen
import com.roman.notificationlistenerexample.ui.theme.NotificationListenerExampleTheme
import com.roman.notificationlistenerexample.ui.view_model.ViewModel
import com.roman.notificationlistenerexample.util.Constants.INTENT_FILTER_ACTION


class MainActivity : ComponentActivity() {

    private val viewModel = ViewModel()
    private lateinit var nService: NotificationService
    private lateinit var nReceiver: NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nService = NotificationService(applicationContext)
        nReceiver = NotificationReceiver(viewModel)
        val filter = IntentFilter(INTENT_FILTER_ACTION)
        registerReceiver(nReceiver, filter)

        askPermission()

        setContent {
            NotificationListenerExampleTheme {
                MainScreen(
                    text = viewModel.text,
                    onClickClearAll = {
                        viewModel.clearAllNotifications(this)
                    },
                    onClickCreateNotify = {
                        nService.showNotification()
                    },
                    onClickList = {
                        viewModel.listNotifications(this)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(nReceiver)
    }

    private fun askPermission() {
        val cn = ComponentName(applicationContext, NLService::class.java)
        val flat: String = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val enabled = flat.contains(cn.flattenToString())

        if (!enabled) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }
    }
}