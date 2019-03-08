
package com.example.litechat.view.activities

import android.annotation.TargetApi
import android.arch.persistence.room.Room
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log

import kotlinx.android.synthetic.main.activity_chat.*
import android.widget.Toast
import java.lang.Double.parseDouble
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.MessageModel
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import java.lang.NumberFormatException
import java.time.Instant
import com.bumptech.glide.Glide
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.AppDatabse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage



class ChatActivity : AppCompatActivity(), ChatContract.CView {

    var numeric=true
    private var myDataset= ArrayList<MessageModel>()
    private lateinit var adapterForChatActivity: AdapterForChatActivity
    private var chatPresenter = ChatPresenter(this)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.litechat.R.layout.activity_chat)

        adapterForChatActivity= AdapterForChatActivity(myDataset,applicationContext)
        recyclerView.apply {
            adapter=adapterForChatActivity
            setHasFixedSize(true)
        }

        // To store chat info passed from Fragment Chat
         AllChatDataModel.otherUserNumber = intent.getStringExtra("string")
         AllChatDataModel.documentPathId=intent.getStringExtra("documentPathId")
         AllChatDataModel.lastUpdated=intent.getStringExtra("lastUpdated")
         var lastSeen = intent.getStringExtra("lastSeen")
         //Log.e("LastSeen" , lastSeen)

        //Update last seen of the user for current chat
         FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).collection("currentChats").whereEqualTo("chatDocumentId" , AllChatDataModel.documentPathId).get().addOnSuccessListener {
             for (i in it)
             {
                 i.reference.update("lastSeen" , Instant.now().epochSecond.toString())
             }
         }

        // to differentiate between group and personal chat
        try
        {
            val num = parseDouble(AllChatDataModel.otherUserNumber)
        }
        catch (e: NumberFormatException)
        {
            numeric = false
        }

        if(numeric)
        {   //To show profile image and name of the other user in current chat
            textViewOtherUser.text = searchContactName(AllChatDataModel.otherUserNumber)
            FirebaseStorage.getInstance().reference.child(AllChatDataModel.otherUserNumber).child("ProfileImage")
              .downloadUrl.addOnSuccessListener { uri ->

              Glide.with(applicationContext).load(uri).into(imageViewOtherPerson)
            }
        }
        else
        {
            //to show group name
            textViewOtherUser.text = AllChatDataModel.otherUserNumber
            // for group icon
           /* FirebaseStorage.getInstance().getReference().child(AllChatDataModel.otherUserNumber).child("ProfileImage")
                .downloadUrl.addOnSuccessListener { uri ->

                Glide.with(applicationContext).load(uri).into(imageViewOtherPerson)
            }*/
        }

        //get old messages of current chat
        chatPresenter.getNewOtherMessagesFromInteractor()

        // to send messages
        buttonSend.setOnClickListener {
                // also change last updated
                if(AllChatDataModel.documentPathId != null)
                {
                    if (!editTextSend.text.toString().isEmpty())
                    {
                        var messageModel= MessageModel()
                        messageModel.message=editTextSend.text.toString()
                        messageModel.sentBy=AllChatDataModel.userNumberIdPM// sala ab bhi null h
                        messageModel.sentOn=Instant.now().epochSecond.toString()
                        editTextSend.setText("")
                        buttonSend.isClickable=false
                        chatPresenter.passNewSetMessageFromViewtoPresenter(messageModel,applicationContext)
                    }
                    else
                    {
                        Toast.makeText(this@ChatActivity,"Please type a message to send",Toast.LENGTH_SHORT).show()
                    }
                }

        }

        // button for image sharing
        buttonCamera.setOnClickListener {

        }


    }

    // method to search contact
    private fun searchContactName(number: String): String {

        var name : String
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()
        try
        {

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
           adapterForChatActivity.notifyDataSetChanged()
       if(myDataset.size>2)
       {recyclerView.smoothScrollToPosition(myDataset.size-1)}
           buttonSend.isClickable=true
       }

    override fun onBackPressed()
    {
        FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).collection("currentChats").whereEqualTo("chatDocumentId" , AllChatDataModel.documentPathId).get().addOnSuccessListener {
            for (i in it)
            {
                i.reference.update("lastSeen" , Instant.now().epochSecond.toString())
            }
            AllChatDataModel.allChatArrayListPersonalStatic.clear()
            AllChatDataModel.flagOnBackPressed = true
            chatPresenter.notifyModelOfBackPressed()
            super.onBackPressed()
        }

    }
 }


