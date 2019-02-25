package com.example.litechat.view.activities

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_group_chat.*
import java.time.Instant

class NewGroupChatActivity : AppCompatActivity() {
    private var groupName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_new_group_chat)

        buttong.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(v: View?) {

                Log.d("check1", "onCLick")

                groupName = num1.getText().toString()
                AllChatDataModel.groupNumbers.add(num2.text.toString())
                AllChatDataModel.groupNumbers.add(num3.text.toString())


                var newObj = NewDocumentCreate(AllChatDataModel.userNumberIdPM, groupName!!)
                Log.d("check5",database.toString())

                database.collection("Chats").document().set(newObj).addOnSuccessListener {

                    database.collection("Chats").whereEqualTo("num", AllChatDataModel.userNumberIdPM)

                        .whereEqualTo("gname", groupName).get().addOnSuccessListener { document ->

                            for (doc in document) {
                                Log.d("check2", doc.toString() + document.toString())
                                var timeStamp = Instant.now().epochSecond
                                for (i in 0 until AllChatDataModel.groupNumbers.size) {
                                    var group = ChatObject()
                                    group.lastUpdated = timeStamp.toString()
                                    group.chatDocumentId = doc.id
                                    group.otherNumber = groupName
                                    Log.d("check", group.toString())
                                    database.collection("Users").document(AllChatDataModel.groupNumbers[i])
                                        .collection("currentPersonalChats").document().set(group)
                                }

                                var me = ChatObject()
                                me.lastUpdated = timeStamp.toString()
                                me.chatDocumentId = doc.id
                                me.otherNumber = groupName
                                database.collection("Users").document(AllChatDataModel.userNumberIdPM)
                                    .collection("currentPersonalChats").document().set(me)
                                    .addOnSuccessListener { result ->
                                        var intent = Intent(this@NewGroupChatActivity, ChatActivity::class.java)
                                        intent.putExtra("documentPathId", me.chatDocumentId)
                                        intent.putExtra("string", me.otherNumber)
                                        intent.putExtra("lastUpdated", me.lastUpdated)
                                        startActivity(intent)
                                        finish()

                                    }
                            }
                        }
                }
            }

        })
    }

    class NewDocumentCreate(num: String, name: String) {
        var Num = num
        var gName = name
    }
}
