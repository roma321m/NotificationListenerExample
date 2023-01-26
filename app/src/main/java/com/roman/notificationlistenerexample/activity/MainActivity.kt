package com.roman.notificationlistenerexample.activity

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.roman.notificationlistenerexample.service.NLService
import com.roman.notificationlistenerexample.service.NLService.Companion.INTENT_FILTER_ACTION
import com.roman.notificationlistenerexample.service.NotificationReceiver
import com.roman.notificationlistenerexample.service.NotificationService
import com.roman.notificationlistenerexample.ui.screens.MainScreen
import com.roman.notificationlistenerexample.ui.theme.NotificationListenerExampleTheme
import com.roman.notificationlistenerexample.ui.view_model.ViewModel


class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val viewModel = ViewModel()
    private lateinit var nService: NotificationService
    private lateinit var nReceiver: NotificationReceiver

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nService = NotificationService(applicationContext)
        nReceiver = NotificationReceiver(viewModel)
        val filter = IntentFilter(INTENT_FILTER_ACTION)
        registerReceiver(nReceiver, filter)

        askPermission()
        askNotificationPermission()
        fcnToken()

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

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) // temp
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun fcnToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, "FCM Token: $token")
        })
    }
}