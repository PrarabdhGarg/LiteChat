package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.litechat.R
import com.example.litechat.contracts.ChatContract
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(),ChatContract.CView {
    override fun displayMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var  AdapterForChatActivity: AdapterForChatActivity
    private lateinit  var mydataset: ArrayList<String>
    private var chatPresenter= ChatPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
  var string = intent.getStringExtra("Number")
        chatPresenter.getMessageFromId(string)

        mydataset.add("Hi")

        AdapterForChatActivity = AdapterForChatActivity(mydataset)
        recyclerView.apply {
            adapter= AdapterForChatActivity
             setHasFixedSize(true)
        }
    }
}
