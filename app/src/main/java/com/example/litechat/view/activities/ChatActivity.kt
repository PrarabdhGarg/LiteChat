package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.litechat.R
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.MessageModel
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.Double.parseDouble

class ChatActivity : AppCompatActivity(), ChatContract.CView {
    /*override fun displayMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/


    private lateinit var AdapterForChatActivity: AdapterForChatActivity
    private var myDataset = ArrayList<MessageModel>()
    private var chatPresenter = ChatPresenter()
    var numeric = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var string = intent.getStringExtra("number")
        try {
            val num = parseDouble(string)


        } catch (e: NumberFormatException) {
            numeric=false

        }
        if (!numeric) {
            var groupChat = AllChatDataModel.allChatArrayListGroupStatic.find { it.otherPerson == string }
            myDataset.addAll(groupChat!!.allMessages)
            AdapterForChatActivity = AdapterForChatActivity(string, myDataset)
            recyclerView.apply {
                adapter = AdapterForChatActivity
                setHasFixedSize(true)

            }


        } else {

            var personalChat = AllChatDataModel.allChatArrayListN1Static.find { it.otherPerson == string }
            myDataset.addAll(personalChat!!.allMessages)
            AdapterForChatActivity = AdapterForChatActivity(string, myDataset)
            recyclerView.apply {
                adapter = AdapterForChatActivity
                setHasFixedSize(true)
            }
        }

    }


}

