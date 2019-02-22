package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.litechat.R
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel

import com.example.litechat.model.MessageList

import com.example.litechat.model.MessageModel
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.Double.parseDouble

import java.lang.NumberFormatException

class ChatActivity : AppCompatActivity(),ChatContract.CView {





    var numeric=true
    private var myDataset= ArrayList<MessageModel>()
    private var groupDataset=ArrayList<MessageModel>()
    private  var adapterForChatActivity: AdapterForChatActivity?=null

    private var chatPresenter = ChatPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        var string = intent.getStringExtra("string")
        try {
            val num = parseDouble(string)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        if (!numeric)
        {
            var groupChat= AllChatDataModel.allChatArrayListGroupStatic.find { it.otherPerson==string}
            Log.d("check",groupChat!!.allMessages.toString())
            groupDataset.addAll(groupChat!!.allMessages)
            adapterForChatActivity= AdapterForChatActivity(string,groupDataset)
            recyclerView.apply {
                adapter=adapterForChatActivity
                setHasFixedSize(true)
            }
            groupDataset.clear()
        }
        else {
        var personalChat=AllChatDataModel.allChatArrayListN1Static.find { it.otherPerson==string }
            myDataset.clear()
            myDataset.addAll(personalChat!!.allMessages)
            adapterForChatActivity= AdapterForChatActivity(string,myDataset)
            recyclerView.apply {
                adapter=adapterForChatActivity
                setHasFixedSize(true)
            }
        }

    }


}


