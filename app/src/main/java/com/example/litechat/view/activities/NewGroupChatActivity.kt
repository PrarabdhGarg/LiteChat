package com.example.litechat.view.activities

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
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

        buttong.setOnClickListener {

            if (ContactListData.groupContacts.size>0) {
                groupLoader.visibility = View.VISIBLE
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Log.d("check1", "onCLick")

                groupName = num1.getText().toString()
                for(i in 0 until ContactListData.groupContacts.size) {
                    AllChatDataModel.groupNumbers.add(ContactListData.groupContacts[i].mobileNumber)
                    Log.d("check",AllChatDataModel.groupNumbers.toString())
                }

                newObj.groupname=groupName
                newObj.groupmembers.addAll(AllChatDataModel.groupNumbers)
                newObj.groupmembers.add(AllChatDataModel.userNumberIdPM)
                newObj.usernumber=AllChatDataModel.userNumberIdPM
                Log.d("check5",newObj.toString())

                // there may be a bug in this query same user creates group with same name might be a problem
                database.collection("Chats").document().set(newObj).addOnSuccessListener {

                    database.collection("Chats").whereEqualTo("usernumber",AllChatDataModel.userNumberIdPM)

                        .whereEqualTo("groupname", groupName).get().addOnSuccessListener { document ->

                            for (doc in document) {
                                Log.d("check2", doc.toString() + document.toString())
                                var timeStamp = Instant.now().epochSecond
                                for (i in 0 until AllChatDataModel.groupNumbers.size) {// since user group creter me do bar child createhora h
                                    var group = ChatObject()
                                    group.lastUpdated = timeStamp.toString()
                                    group.chatDocumentId = doc.id
                                    group.otherNumber = groupName
                                    group.lastSeen=timeStamp.toString()
                                    Log.d("check", group.toString())
                                    database.collection("Users").document(AllChatDataModel.groupNumbers[i])
                                        .collection("currentChats").document().set(group)
                                }

                                var me = ChatObject()
                                me.lastUpdated = timeStamp.toString()
                                me.chatDocumentId = doc.id
                                me.otherNumber = groupName
                                me.lastSeen= timeStamp.toString()
                                database.collection("Users").document(AllChatDataModel.userNumberIdPM)
                                    .collection("currentChats").document().set(me)
                                    .addOnSuccessListener { result ->
                                        var intent = Intent(this@NewGroupChatActivity, ChatActivity::class.java)
                                        intent.putExtra("documentPathId", me.chatDocumentId)
                                        intent.putExtra("string", me.otherNumber)
                                        intent.putExtra("lastUpdated", me.lastUpdated)
                                        groupLoader.visibility = View.INVISIBLE
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                        AllChatDataModel.groupNumbers.clear()
                                        ContactListData.groupContacts.clear()
                                        startActivity(intent)

                                        finish()

                                    }
                            }
                        }
                }
            }
            else{

                Toast.makeText(applicationContext, "Please select atleast 1 contact",Toast.LENGTH_SHORT).show()
            }
        }
    }

}

