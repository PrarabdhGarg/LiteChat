package com.example.litechat.view.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.litechat.ListenerObjectTry
import com.example.litechat.R
import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.MessageList
import com.example.litechat.presenter.FragmentChatPresenter
import com.example.litechat.view.activities.ChatActivity
import com.example.litechat.view.adapters.AdapterForFragmentChat
import kotlinx.android.synthetic.main.fragment_chat.*

class FragmentChat : Fragment(), AllChatsContractFrag.CFView {


    /*private var ngroupChatNames=ArrayList<String>()
    private var personalChatNamesN1=ArrayList<String>()
    private var personalChatNamesN2=ArrayList<String>()
   */
    private var personalChatsForActivity = ArrayList<MessageList>()

    private lateinit var listenerForChat: ListenerObjectTry
    private lateinit var listenerForProfile: ListenerObjectTry
    private var chatNamesForFragment = ArrayList<ChatObject>()
    private var frag = FragmentChatPresenter(this)
    private  var adapterForFragmentChat: AdapterForFragmentChat? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("ViewPager" , "onCreateView of FragmentChat called")
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Listeners For calling ChatActivity on click event on RecyclerView
        listenerForChat = ListenerObjectTry()
        listenerForProfile = ListenerObjectTry()
        listenerForChat.setCustomObjectListener(object : ListenerObjectTry.Listener {

            override fun onDataRecieved(number: String, chatDocumentId: String,lastUpdated:String) {
                val intent = Intent(context, ChatActivity::class.java)
                Toast.makeText(context,number,Toast.LENGTH_LONG).show()
                intent.putExtra("documentPathId",chatDocumentId)
                intent.putExtra("string",number)
                intent.putExtra("lastUpdated",lastUpdated)
                AllChatDataModel.personalChatList.clear()
                startActivity(intent)
            }
        })


        listenerForProfile.setCustomObjectListener(object : ListenerObjectTry.Listener {

            override fun onDataRecieved(number: String, chatDocumentId: String,lastUpdated:String) {

                /** for opening profile
                 *  val intent = Intent(context,ChatActivity::class.java)
                intent.putExtra("Number","9826936889")
                startActivity(intent)
                 * */

            }
        })
            adapterForFragmentChat = AdapterForFragmentChat(chatNamesForFragment, context!!, listenerForProfile, listenerForChat)
            Log.d("FinalDebug15" , adapterForFragmentChat.toString())
            view.findViewById<RecyclerView>(R.id.recyView_FragmentChat).adapter = adapterForFragmentChat

        return view
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("ViewPager" , "onCreate of FragmentChat called")
        AllChatDataModel.upadateFragmentChatFirstTime = 1
        super.onCreate(savedInstanceState)
    }

    override fun setGroupNames(groupChatNames: ArrayList<String>) {


        /*chatNamesForFragment.addAll(groupChatNames)
        Log.d("QueryF1",chatNamesForFragment.toString())
            adapterForFragmentChat!!.notifyDataSetChanged()*/
    }

    override fun setPersonalChatNames(personalChatNames: ArrayList<String>) {
      /* chatNamesForFragment.addAll(personalChatNames)

        adapterForFragmentChat!!.notifyDataSetChanged()*/
    }


    override fun setPersonalChatN1(personalChatNamesN1: ArrayList<MessageList>) {
        /* chatNamesForFragment.addAll(personalChatNamesN1.)
         adapterForFragmentChat.notifyDataSetChanged()
         */
    }

    override fun setPersonalChatN2(personalChatNamesN2: ArrayList<MessageList>) {
        //  this.personalChatNamesN2 = personalChatNamesN2
        /*chatNamesForFragment.addAll(personalChatNamesN2)
        adapterForFragmentChat.notifyDataSetChanged()*/
    }


    override fun setContactstoFragmentChat() {

    }


    override fun updateRecyclerView() {
        Log.d("FinalDebug9", "upadte all start  size${AllChatDataModel.personalChatList}")
        //chatNamesForFragment.clear()
        //chatNamesForFragment.addAll(AllChatDataModel.personalChatList)
        /*AllChatDataModel.personalChatList.clear()*/
        //Log.d("FinalDebug10", "upadte all start chatfor size${chatNamesForFragment.last()}")
        //chatNamesForFragment.add(chatNamesForFragment.last())
        if(chatNamesForFragment.size==0){
            chatNamesForFragment.addAll(AllChatDataModel.personalChatList)
        }
        adapterForFragmentChat!!.notifyDataSetChanged()

    }

    override fun updateRecyclerViewForFirstTime() {
        Log.d("Dataa","first time code start ${adapterForFragmentChat}")
        chatNamesForFragment.clear()
        chatNamesForFragment.addAll(AllChatDataModel.personalChatList)
        AllChatDataModel.personalChatList.clear()
        Log.d("Debug 13" , AllChatDataModel.personalChatList.size.toString())
        adapterForFragmentChat!!.notifyDataSetChanged()

    }

    override fun onDestroy() {
        Log.d("ViewPager" , "onDestroy of FragmentChat called")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("ViewPager" , "onDestroyView of FragmentChat called")
        super.onDestroyView()
    }



}