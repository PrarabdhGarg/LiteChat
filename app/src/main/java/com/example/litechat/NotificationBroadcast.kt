package com.example.litechat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationBroadcast : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Notification" , "Broadcast Reciver Called")

        context?.startService(Intent(context , NotificationService::class.java))
    }

}