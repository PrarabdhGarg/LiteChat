package com.example.litechat.view.activities

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.litechat.R
import com.example.litechat.listeners.ListenerForFragmentChat
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListModel
import com.example.litechat.view.adapters.DeveloperAdapter
import com.example.litechat.view.adapters.SearchResultsAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_developer.*
import kotlinx.android.synthetic.main.activity_search_results.*
import java.lang.Exception

class SearchResultsActivity : AppCompatActivity() {
      var result=ArrayList<ChatObject>()
    private var listenerForChat= ListenerForFragmentChat()
    private var listenerForProfile=ListenerForFragmentChat()
    private  var numeric =true
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        Log.d("check","called")
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH .equals(intent.action)) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.d("check","justout")
            viewManager = LinearLayoutManager(applicationContext)
           for(i in 0 until AllChatDataModel.personalChatList.size) {
               var user=ContactListModel().roomGetName(this , AllChatDataModel.personalChatList[i].otherNumber)
               if (user.contains(query,true)) {
                   Log.d("check", "entered")
                    result.add(AllChatDataModel.personalChatList[i])
               }
           }

            listenerForChat.setCustomObjectListener(object : ListenerForFragmentChat.Listener {

                override fun onDataRecieved(number: String, chatDocumentId: String,lastUpdated:String, url : String) {
                    val intent = Intent(this@SearchResultsActivity, ChatActivity::class.java)
                    Toast.makeText(this@SearchResultsActivity,number,Toast.LENGTH_LONG).show()
                    intent.putExtra("documentPathId",chatDocumentId)
                    intent.putExtra("string",number)
                    intent.putExtra("lastUpdated",lastUpdated)
                    AllChatDataModel.personalChatList.clear()
                    AllChatDataModel.chatScreenStatus = 1
                    startActivity(intent)
                }
            })

            listenerForProfile.setCustomObjectListener(object : ListenerForFragmentChat.Listener {

                override fun onDataRecieved(number: String, chatDocumentId: String,lastUpdated:String, url :String) {
                    try
                    {
                        var num= number.toDouble()
                    }catch (e : Exception){
                        numeric = false

                    }

                    if(numeric)
                    {   //  to show contact name of person chatting with
                        var intent = Intent(this@SearchResultsActivity,ProfileOtherUser::class.java)
                        intent.putExtra("number" , number)
                        AllChatDataModel.chatScreenStatus = 1
                        startActivity(intent)
                    }
                    else
                    {
                        var intent=Intent(this@SearchResultsActivity,GroupInfoActivity::class.java)
                        Log.d("GroupInfo1","documentPathId$chatDocumentId")
                        intent.putExtra("documentPathId",chatDocumentId)
                        AllChatDataModel.chatScreenStatus = 1
                        startActivity(intent)
                    }

                }
            })
            var viewAdapter = SearchResultsAdapter(result,this,listenerForProfile,listenerForChat)
            searchRecycler.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }

        }
    }


}
