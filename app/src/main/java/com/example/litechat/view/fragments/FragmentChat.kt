package com.example.litechat.view.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litechat.ListenerObjectTry
import com.example.litechat.R
import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.contracts.ChatContract
import com.example.litechat.presenter.FragmentChatPresenter
import com.example.litechat.view.activities.ChatActivity
import com.example.litechat.view.adapters.AdapterForFragmentChat
import kotlinx.android.synthetic.main.fragment_chat.*

class FragmentChat: Fragment(),AllChatsContractFrag.CFView{

    private lateinit var  listenerForChat: ListenerObjectTry
   private  lateinit var listenerForProfile: ListenerObjectTry
   // private var dataset =ArrayList<String>()
    private var frag = FragmentChatPresenter(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

       frag.getChats()
        /**
         * Listener for calling personal chat activity
         * */
        listenerForChat= ListenerObjectTry()
        listenerForProfile=ListenerObjectTry()
        listenerForChat.setCustomObjectListener(object : ListenerObjectTry.Listener{

            override fun onDataRecieved(number: String) {
                val intent = Intent(context,ChatActivity::class.java)
                //removed PutExtra Method
                startActivity(intent)

            }
        })


        listenerForProfile.setCustomObjectListener(object : ListenerObjectTry.Listener{

            override fun onDataRecieved(number: String) {

                /** for opening profile
                 *  val intent = Intent(context,ChatActivity::class.java)
                intent.putExtra("Number","9826936889")
                startActivity(intent)
                 * */

            }
        })




        return view
    }

    override fun setContactstoFragmentChat() {
        var   adapterForFragmentChat= AdapterForFragmentChat(/*dataset,*/context!!,listenerForProfile,listenerForChat)
        recyView_FragmentChat.adapter= adapterForFragmentChat

    }

}