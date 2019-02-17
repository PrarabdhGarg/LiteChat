package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.litechat.R
import com.example.litechat.contracts.ChatContract
import com.example.litechat.view.adapters.ChatAdapter
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(),ChatContract.CView {
    override fun displayMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var  ChatAdapter: ChatAdapter
    private lateinit  var mydataset: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mydataset.add("Hi")

        ChatAdapter = ChatAdapter(mydataset)
        recyclerView.apply {
            adapter= ChatAdapter
             setHasFixedSize(true)
        }
    }
}
