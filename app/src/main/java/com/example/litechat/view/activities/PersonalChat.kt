package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.litechat.R
import com.example.litechat.view.adapters.PersonalChatAdapter
import kotlinx.android.synthetic.main.activity_personal_chat.*

class PersonalChat : AppCompatActivity() {

    private lateinit var  personalChatAdapter: PersonalChatAdapter
    private lateinit  var mydataset: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_chat)

        mydataset.add("Hii")

        personalChatAdapter = PersonalChatAdapter(mydataset)
        recyclerView.apply {
            adapter= personalChatAdapter
             setHasFixedSize(true)
        }
    }
}
