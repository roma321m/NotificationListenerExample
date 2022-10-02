package com.roman.notificationlistenerexample.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.roman.notificationlistenerexample.util.Constants.INTENT_FILTER_ACTION

class NLService : NotificationListenerService() {

    companion object {
        const val TAG = "NotificationListener"
    }

    private lateinit var nlServiceReceiver : NLServiceReceiver

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        nlServiceReceiver = NLServiceReceiver()
        val filter = IntentFilter(INTENT_FILTER_ACTION)
        registerReceiver(nlServiceReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(nlServiceReceiver)
        Log.d(TAG, "onDestroy")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d(TAG, "onNotificationPosted")
        sbn?.let {
            Log.d(TAG, "Id: ${sbn.id}  ${sbn.notification.tickerText}  ${sbn.packageName}")
            val intent = Intent(INTENT_FILTER_ACTION)
            intent.putExtra("notification_event", "onNotificationPosted : ${sbn.packageName} n")
            sendBroadcast(intent)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d(TAG, "onNotificationRemoved")
        sbn?.let {
            Log.d(TAG, "Id: ${sbn.id}  ${sbn.notification.tickerText}  ${sbn.packageName}")
            val intent = Intent(INTENT_FILTER_ACTION)
            intent.putExtra("notification_event", "onNotificationRemoved : ${sbn.packageName} n")
            sendBroadcast(intent)
        }
    }

    override fun onNotificationRankingUpdate(rankingMap: RankingMap?) {
        super.onNotificationRankingUpdate(rankingMap)
        Log.d(TAG, "onNotificationRemoved")
    }

    internal inner class NLServiceReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.getStringExtra("command") == "clearall") {
                    this@NLService.cancelAllNotifications()
                } else if (it.getStringExtra("command") == "list") {
                    val i1 = Intent(INTENT_FILTER_ACTION)
                    i1.putExtra("notification_event", "=======================")
                    sendBroadcast(i1)
                    var i = 1
                    for (sbn in this@NLService.activeNotifications) {
                        val i2 = Intent(INTENT_FILTER_ACTION)
                        i2.putExtra("notification_event", "$i ${sbn.packageName}")
                        sendBroadcast(i2)
                        i++
                    }
                    val i3 = Intent(INTENT_FILTER_ACTION)
                    i3.putExtra("notification_event", "===== Notification List ====")
                    sendBroadcast(i3)
                }
            }
        }
    }
}