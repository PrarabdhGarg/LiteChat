package com.example.litechat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.example.litechat.model.ContactListModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*

class NotificationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    var snapListener : ListenerRegistration? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Notifications" , "Service Started")
       // Toast.makeText(this , "Service Started " , Toast.LENGTH_SHORT).show()
        FirebaseApp.initializeApp(this)
        //serviceForNotification(this).execute()
        var i = 0
        val ref = FirebaseFirestore.getInstance()
        /**
         * Replace hard-coded mobile number with users mobile number taken from shared preferences
         */
        var userNumber = PreferenceManager.getDefaultSharedPreferences(this).getString("CurrentUserNumber" , "123456789").toString()
        snapListener = ref.collection("Users").document(userNumber).collection("currentChats")
            .addSnapshotListener(
                MetadataChanges.INCLUDE, EventListener<QuerySnapshot> { snap, e ->
                    for (dc in snap!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.MODIFIED -> {
                                Log.d("Notification" , "Entered modified block")
                                Toast.makeText(this , "Added the block for Modified" , Toast.LENGTH_SHORT).show()
                                var manger : NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                manger.createNotificationChannel(NotificationChannel(dc.document["otherNumber"].toString() , "Notification1" , NotificationManager.IMPORTANCE_DEFAULT))
                                val mBuilder = NotificationCompat.Builder(this)
                                    .setSmallIcon(com.example.litechat.R.drawable.profile)
                                    .setContentTitle("New Message from " + ContactListModel().roomGetName(this , dc.document["otherNumber"].toString()))
                                    .setContentText("Pls look at the message")
                                    .setOnlyAlertOnce(true)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setChannelId(dc.document["otherNumber"].toString())
                                mBuilder.setAutoCancel(false)
                                mBuilder.setLocalOnly(true)
                                Log.d("Notifications" , "Try Notifications entered with ${mBuilder.build()}")
                                manger.notify(Integer.parseInt(dc.document["otherNumber"].toString()) , mBuilder.build())
                                i++
                            }
                        }
                    }

                })
        return START_STICKY   //This return statement ensures android does not kill this service until and unless the resources are extremely scarce
    }

    override fun onDestroy() {
        Log.d("Notification" , "onDestroy of service called")
      //  Toast.makeText(this , "ServiceDestroyed" , Toast.LENGTH_SHORT).show()
        snapListener!!.remove()
        sendBroadcast(Intent(this , NotificationBroadcast::class.java))
        super.onDestroy()
    }

    /*override fun onNewToken(p0: String?) {
        Log.d("Notifications" , "New token generated ${p0}")
        UserProfileData.UserToken = p0
        super.onNewToken(p0)
    }*/
}
