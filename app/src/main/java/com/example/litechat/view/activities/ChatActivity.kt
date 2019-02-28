package com.example.litechat.view.activities

import android.annotation.TargetApi
import android.arch.persistence.room.Room
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import kotlinx.android.synthetic.main.activity_chat.*

import com.example.litechat.R

import android.view.View
import java.lang.Double.parseDouble

import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel

import com.example.litechat.model.MessageModel
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.Double.parseDouble

import java.lang.NumberFormatException
import java.time.Instant
import android.support.v7.widget.RecyclerView
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.AppDatabse
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log


class ChatActivity : AppCompatActivity(), ChatContract.CView {





    var numeric=true
    private var myDataset= ArrayList<MessageModel>()
    private var groupDataset=ArrayList<MessageModel>()
    private lateinit var adapterForChatActivity: AdapterForChatActivity

    private var chatPresenter = ChatPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.litechat.R.layout.activity_chat)

        adapterForChatActivity= AdapterForChatActivity(myDataset,applicationContext)


        recyclerView.apply {
            adapter=adapterForChatActivity
            setHasFixedSize(true)

        }


         AllChatDataModel.otherUserNumber = intent.getStringExtra("string")
         AllChatDataModel.documentPathId=intent.getStringExtra("documentPathId")
         AllChatDataModel.lastUpdated=intent.getStringExtra("lastUpdated")
         var lastSeen = intent.getStringExtra("lastSeen")
         //Log.e("LastSeen" , lastSeen)
         FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).collection("currentChats").whereEqualTo("chatDocumentId" , AllChatDataModel.documentPathId).get().addOnSuccessListener {
             for (i in it)
             {
                 i.reference.update("lastSeen" , Instant.now().epochSecond.toString())
             }
         }
        try
        {
            val num = parseDouble(AllChatDataModel.otherUserNumber)
        }
        catch (e: NumberFormatException)
        {
            numeric = false
        }

        if(numeric)
        {   //  to show contact name of person chatting with
            textViewOtherUser.setText(searchContactName(AllChatDataModel.otherUserNumber))
        }
        else
        {
            textViewOtherUser.setText(AllChatDataModel.otherUserNumber)
        }



            // get previous chats and caching
            chatPresenter.getNewOtherMessagesFromInteractor()

        // handle when message is sent
        buttonSend.setOnClickListener {
            // also change last updated
            Log.d("saala","meonclick")
            var messageModel= MessageModel()
            messageModel.message=editTextSend.text.toString()
            messageModel.sentBy=AllChatDataModel.userNumberIdPM// sala ab bhi null h
            messageModel.sentOn=Instant.now().epochSecond.toString()
            editTextSend.setText("")
            buttonSend.isClickable=false;
            chatPresenter.passNewSetMessageFromViewtoPresenter(messageModel,applicationContext)
        }


        buttonCamera.setOnClickListener {

        }


    }

    private fun searchContactName(number: String): String {
             var name : String
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()
        try {

            name= db.userDao().getName(AllChatDataModel.otherUserNumber)
            Log.d("debug",name)
            return name
        }

        catch (e: Exception){
            Log.d("debug",e.toString())
            return  number
        }



    }

    override fun getOtherMessagesFromPresenter() {

    }

    override fun displayMessage()
    {
            Log.d("Run4","code of displayNewMessage")
          // claer previous messages from adapeter datset to avoid appending of messages
           myDataset.clear()
           myDataset.addAll(AllChatDataModel.allChatArrayListPersonalStatic)

           Log.d("Run7",myDataset.size.toString())
           adapterForChatActivity!!.notifyDataSetChanged()
       if(myDataset.size>2)
       {recyclerView.smoothScrollToPosition(myDataset.size-1)}
           buttonSend.isClickable=true
       }

    override fun onBackPressed()
    {
        AllChatDataModel.allChatArrayListPersonalStatic.clear()
        AllChatDataModel.flagOnBackPressed = true
        chatPresenter.notifyModelOfBackPressed()
        super.onBackPressed()
    }
 }


