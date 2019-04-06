package com.example.litechat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.example.litechat.model.AllChatDataModel.lastUpdated
import com.example.litechat.model.ContactListModel
import com.example.litechat.view.activities.ChatActivity
import com.example.litechat.view.activities.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    override fun onCreate() {
        Log.d("Notification" , "Service Called")
        super.onCreate()
    }

    override fun onNewToken(p0: String?) {
        Log.d("Notification" , "New Token generated\n$p0")
        var firebase = FirebaseFirestore.getInstance()
        Log.d("Notification" , "Reference is $firebase")
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        Log.d("Notification" , "Message recived is ${p0!!.data}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Chats"
            val descriptionText = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        super.onMessageReceived(p0)
        if(p0 != null)
        {
            var intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra("documentPathId",p0.data["document"])
            intent.putExtra("string",p0.data["title"])
            intent.putExtra("lastUpdated",p0.data["lastUpdated"])
            Log.d("Notification" , "Intent of service ${intent.extras.toString()}")
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            //TODO Change the icon of notifications
            var builder = NotificationCompat.Builder(this , "1")
                .setSmallIcon(R.drawable.chat_button)
                .setContentTitle(ContactListModel().roomGetName(this , p0.data["title"]!!))
                .setContentText(p0.data["body"])
                .setStyle(NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(Integer.parseInt(p0.data["messageId"]!!) , builder)
            }
        }

    }

}
