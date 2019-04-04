package com.example.litechat

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String?) {
        Log.d("Notification" , "New Token generated\n$p0")
        var firebase = FirebaseFirestore.getInstance()
        Log.d("Notification" , "Reference is $firebase")
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        var firebase = FirebaseFirestore.getInstance()
        Log.d("Notification" , "Reference is $firebase")
        super.onMessageReceived(p0)
    }

}
