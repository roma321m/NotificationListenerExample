package com.roman.notificationlistenerexample.activity

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
}