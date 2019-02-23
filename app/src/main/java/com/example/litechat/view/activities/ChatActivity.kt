package com.example.litechat.view.activities

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.litechat.R
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel

import com.example.litechat.model.MessageList

import com.example.litechat.model.MessageModel
import com.example.litechat.model.UserProfileData
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.Double.parseDouble

import java.lang.NumberFormatException
import java.time.Instant

class ChatActivity : AppCompatActivity(),ChatContract.CView {





    var numeric=true
    private var myDataset= ArrayList<MessageModel>()
    private var groupDataset=ArrayList<MessageModel>()
    private lateinit var adapterForChatActivity: AdapterForChatActivity

    private var chatPresenter = ChatPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        adapterForChatActivity= AdapterForChatActivity(myDataset)
        recyclerView.apply {
            adapter=adapterForChatActivity
            setHasFixedSize(true)
        }

        buttonSend.setOnClickListener(object : View.OnClickListener{

            @TargetApi(Build.VERSION_CODES.O)
            override fun onClick(v: View?) {
                //AllChatDataModel.flag=true
               var messageModel= MessageModel()
                messageModel.message=editTextSend.text.toString()
                messageModel.sentBy="9826936889"// sala ab bhi null h
                messageModel.sentOn=Instant.now().epochSecond.toString()
                chatPresenter.passNewSetMessageFromViewtoPresenter(messageModel,applicationContext)


            }
        })
         AllChatDataModel.numberID = intent.getStringExtra("string")
        try {
            val num = parseDouble(AllChatDataModel.numberID)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        if (!numeric)
        {
            var groupChat= AllChatDataModel.allChatArrayListGroupStatic.find { it.otherPerson==AllChatDataModel.numberID}
            groupDataset.addAll(groupChat!!.allMessages)

        }
        else {
            //pass documentId in it

          /* var personalChat=AllChatDataModel.allChatArrayListN1Static.find { it.otherPerson==AllChatDataModel.numberID }
            Log.d("Run5",personalChat!!.allMessages.size.toString())
            myDataset.clear()
            myDataset.addAll(personalChat!!.allMessages)
            Log.d("Run6",myDataset.size.toString())*/

            chatPresenter.getNewOtherMessagesFromInteractor()
        }





    }
    override fun getOtherMessagesFromPresenter() {

    }

    override fun displayMessage() {
            Log.d("Run4","code of displayNewMessage")
           // yh kch sochna padega sare clear krke add nhi krne h
        myDataset.clear()
        myDataset.addAll(AllChatDataModel.allChatArrayListPersonalStatic)
        //   myDataset.add(AllChatDataModel.allChatArrayListPersonalStatic.last())
           //AllChatDataModel.allChatArrayListN1Static.find { it.otherPerson==AllChatDataModel.numberID }!!.allMessages.add(AllChatDataModel.allChatArrayListPersonalStatic.last())
           Log.d("Run7",myDataset.size.toString())
           adapterForChatActivity!!.notifyDataSetChanged()
       }

    override fun onBackPressed() {
      //  AllChatDataModel.flag=false
       /* var objMessageList= MessageList()
        objMessageList.otherPerson=AllChatDataModel.numberID
        objMessageList.allMessages.addAll(AllChatDataModel.allChatArrayListPersonalStatic)
        Log.d("Size1",objMessageList.allMessages.size.toString()+"\n"+AllChatDataModel.allChatArrayListPersonalStatic.size.toString())
        Log.d("Size2","\n"+AllChatDataModel.allChatArrayListN1Static.size.toString())
        AllChatDataModel.allChatArrayListN1Static.filter { it.otherPerson==AllChatDataModel.numberID }
       AllChatDataModel.allChatArrayListN1Static. addAll()*/
        AllChatDataModel.allChatArrayListPersonalStatic.clear()
        super.onBackPressed()
    }
    }




