package com.example.litechat.view.activities

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

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



class ChatActivity : AppCompatActivity(),ChatContract.CView {





    var numeric=true
    private var myDataset= ArrayList<MessageModel>()
    private var groupDataset=ArrayList<MessageModel>()
    private lateinit var adapterForChatActivity: AdapterForChatActivity

    private var chatPresenter = ChatPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.litechat.R.layout.activity_chat)

        adapterForChatActivity= AdapterForChatActivity(myDataset)


        recyclerView.apply {
            adapter=adapterForChatActivity
            setHasFixedSize(true)

        }


         AllChatDataModel.otherUserNumber = intent.getStringExtra("string")
         AllChatDataModel.documentPathId=intent.getStringExtra("documentPathId")
         AllChatDataModel.lastUpdated=intent.getStringExtra("lastUpdated")
        try
        {
             val num = parseDouble(AllChatDataModel.otherUserNumber)
        }
        catch (e: NumberFormatException)
        {
            numeric = false
        }
       /* if (!numeric)
        {
            var groupChat= AllChatDataModel.allChatArrayListGroupStatic.find { it.otherPerson==AllChatDataModel.otherUserNumber}
            groupDataset.addAll(groupChat!!.allMessages)

        }
        else
        {*/
            // get previous chats and caching
            chatPresenter.getNewOtherMessagesFromInteractor()
      //  }

        // handle when message is sent
        buttonSend.setOnClickListener(object : View.OnClickListener{

            @TargetApi(Build.VERSION_CODES.O)
            override fun onClick(v: View?) {
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
        })




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




