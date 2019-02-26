package com.example.litechat.view.activities

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListData
import com.example.litechat.model.NewDocumentCreate
import com.example.litechat.view.adapters.GroupContactAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_group_chat.*
import java.time.Instant

class NewGroupChatActivity : AppCompatActivity() {
    private var groupName: String? = null
    private var newObj = NewDocumentCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_new_group_chat)
        var adapt=GroupContactAdapter(this)
        recyclerView2.apply {
            adapter=adapt
            layoutManager=LinearLayoutManager(this@NewGroupChatActivity)
        }
        buttong.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(v: View?) {

                Log.d("check1", "onCLick")

                groupName = num1.getText().toString()
                for(i in 0 until ContactListData.groupContacts.size)
                {
               AllChatDataModel.groupNumbers.add(ContactListData.groupContacts[i].mobileNumber)
                    Log.d("check",AllChatDataModel.groupNumbers.toString())
                }

                newObj.groupname=groupName
                newObj.groupmembers.addAll(AllChatDataModel.groupNumbers)
                newObj.groupmembers.add(AllChatDataModel.userNumberIdPM)
                newObj.usernumber=AllChatDataModel.userNumberIdPM
                Log.d("check5",newObj.toString())

                database.collection("Chats").document().set(newObj).addOnSuccessListener {

                    database.collection("Chats").whereEqualTo("usernumber",AllChatDataModel.userNumberIdPM)

                        .whereEqualTo("groupname", groupName).get().addOnSuccessListener { document ->

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

}

