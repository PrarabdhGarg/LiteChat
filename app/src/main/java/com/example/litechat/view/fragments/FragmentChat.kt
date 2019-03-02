package com.example.litechat.view.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.litechat.listeners.BoomListener
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.MessageList
import com.example.litechat.model.UserProfileData
import com.example.litechat.presenter.FragmentChatPresenter
import com.example.litechat.view.activities.*
import com.example.litechat.view.adapters.AdapterForFragmentChat
import com.google.firebase.auth.FirebaseAuth
import com.nightonke.boommenu.BoomButtons.HamButton
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.lang.Double
import java.lang.NumberFormatException

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
    private var numeric=true
    private  var adapterForFragmentChat: AdapterForFragmentChat? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("ViewPager" , "onCreateView of FragmentChat called")
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val bmbListener1 = BoomListener()
        bmbListener1.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(activity, ProfileActivity::class.java))
            }

        })

        val bmbListener2 = BoomListener()
        bmbListener2.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(activity, NewGroupChatActivity::class.java))
            }

        })

        val bmbListener3 = BoomListener()
        bmbListener3.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(activity, DeveloperActivity::class.java))
            }

        })

        val bmbListener4 = BoomListener()
        bmbListener4.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                FirebaseAuth.getInstance().signOut()
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("CurrentUserNumber" , "").apply()
                UserProfileData.clearData()
                startActivity(Intent(activity , LoginActivity::class.java))
            }

        })

        val icons = arrayOf(R.drawable.ic_profile, R.drawable.ic_group, R.drawable.ic_dev, R.drawable.ic_logout)
        val text = arrayOf("PROFILE", "NEW GROUP", "DEVELOPERS", "LOGOUT")
        val bmbListener = arrayOf(bmbListener1, bmbListener2, bmbListener3, bmbListener4)
        view.bmbChat.bringToFront()

        for(i in 0 until view.bmbChat.buttonPlaceEnum.buttonNumber()){

            val builder: HamButton.Builder = HamButton.Builder()
                .normalImageRes(icons[i])
                .normalText(text[i])
                .pieceColor(Color.WHITE)
                .shadowEffect(true)
                .rippleEffect(true)
                .textSize(24)
                .textPadding(Rect(40,0,0,0))
                .imagePadding(Rect(20,0,20,0))
                .listener { index ->
                    bmbListener[index].listener!!.doThis()
                }

            view.bmbChat.addBuilder(builder)
        }
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

                try
                {
                    val num = Double.parseDouble(number)
                }
                catch (e: NumberFormatException)
                {
                    numeric = false
                }

                if(numeric)
                {   //  to show contact name of person chatting with
                    var intent = Intent(context,ProfileOtherUser::class.java)
                    intent.putExtra("number" , number)
                    startActivity(intent)
                }
                else
                {
                    var intent=Intent(context,GroupInfoActivity::class.java)
                    Log.d("GroupInfo1","documentPathId$chatDocumentId")
                    intent.putExtra("documentPathId",chatDocumentId)
                    startActivity(intent)
                }


            }
        })
            adapterForFragmentChat = AdapterForFragmentChat(chatNamesForFragment, context!!, listenerForProfile, listenerForChat)
            Log.d("FinalDebug15" , adapterForFragmentChat.toString())
            view.findViewById<RecyclerView>(R.id.recyView_FragmentChat).adapter = adapterForFragmentChat

        return view
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("ViewPager" , "onCreate of FragmentChat called")
        //AllChatDataModel.upadateFragmentChatFirstTime = 1
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
        /*if(chatNamesForFragment.size==0){
            chatNamesForFragment.addAll(AllChatDataModel.personalChatList)
        }*/
        adapterForFragmentChat!!.notifyDataSetChanged()
        AllChatDataModel.isPresenterCalled = false

    }

    override fun updateRecyclerViewForFirstTime() {
        Log.d("Dataa","first time code start ${adapterForFragmentChat}")
        chatNamesForFragment.clear()
        chatNamesForFragment.addAll(AllChatDataModel.personalChatList)
        //AllChatDataModel.personalChatList.clear()
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