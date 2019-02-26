package com.example.litechat.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litechat.R
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.listeners.BoomListener
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListData
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.ContactFragPresenter
import com.example.litechat.view.activities.*
import com.example.litechat.view.adapters.ContactAdapter
import com.google.firebase.auth.FirebaseAuth
import com.nightonke.boommenu.BoomButtons.HamButton
import kotlinx.android.synthetic.main.fragment_contact.view.*
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.util.ArrayList

class FragmentContact : Fragment(), ContactFragContract.View {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var contactPresenter: ContactFragPresenter
    lateinit var activitySet: FragmentActivity
    lateinit var task : AsyncTask<Void , Void , Void>

    override fun startChatActivity(chatObject: ChatObject) {
        var intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("documentPathId",chatObject.chatDocumentId)
        intent.putExtra("string", chatObject.otherNumber)
        intent.putExtra("lastUpdated",chatObject.lastUpdated)
        startActivity(intent)
    }


    inner class getingContacts(val contactPresenter: ContactFragPresenter) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {

                contactPresenter.getContacts()
                ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>
            activitySet.runOnUiThread {

                viewAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val bmbListener1 = BoomListener()
        bmbListener1.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                startActivity(Intent(activity, ProfileActivity::class.java))
            }

        })

        val bmbListener2 = BoomListener()
        bmbListener2.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                startActivity(Intent(activity, NewGroupChatActivity::class.java))
            }

        })

        val bmbListener3 = BoomListener()
        bmbListener3.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {

                startActivity(Intent(activity, DeveloperActivity::class.java))
            }

        })

        val bmbListener4 = BoomListener()
        bmbListener4.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                FirebaseAuth.getInstance().signOut()
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("CurrentUserNumber" , "").apply()
                UserProfileData.clearData()
                startActivity(Intent(activity , LoginActivity::class.java))
            }

        })

        val icons = arrayOf(R.drawable.ic_profile, R.drawable.ic_group, R.drawable.ic_dev, R.drawable.ic_logout)
        val text = arrayOf("PROFILE", "NEW GROUP", "DEVELOPERS", "LOGOUT")
        val bmbListener = arrayOf(bmbListener1, bmbListener2, bmbListener3, bmbListener4)

        view.bmbContact.bringToFront()

        for(i in 0 until view.bmbContact.buttonPlaceEnum.buttonNumber()){

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

            view.bmbContact.addBuilder(builder)
        }

        contactPresenter = ContactFragPresenter(this, context!!)

        ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>


        val callingListener1 = CallListenerObject()
        callingListener1.setListener(object : CallListenerObject.CallListener {

            override fun startCallIntent(number: String) {

                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            }
        })

        val callingListener2 = CallListenerObject()
        callingListener2.setListener(object : CallListenerObject.CallListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun startCallIntent(number: String) {

              contactPresenter.startNewChatFromContact(number)
                Log.d("Context",context.toString())

            }
        })

        viewManager = LinearLayoutManager(activity)
        viewAdapter = ContactAdapter(callingListener1, callingListener2, context!!)
        view!!.contactRecycler.apply {

            layoutManager = viewManager
            adapter = viewAdapter
        }

/*
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

           *//* if (contactPresenter.passUserList().isEmpty()) {

                contactPresenter.getContacts()

            }*//*
            //getingContacts(contactPresenter).execute()
        }*/


        //ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onStart() {
        super.onStart()

        activitySet = activity!!
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        task = getingContacts(contactPresenter).execute()
        }
//        val callingListener1 = CallListenerObject()
//        callingListener1.setListener(object : CallListenerObject.CallListener {
//
//            override fun startCallIntent(number: String) {
//
//                val intent = Intent(Intent.ACTION_CALL)
//                intent.data = Uri.parse("tel:$number")
//                startActivity(intent)
//            }
//        })
//
//        val callingListener2 = CallListenerObject()
//        callingListener2.setListener(object : CallListenerObject.CallListener {
//
//            override fun startCallIntent(number: String) {
//
//                startActivity(Intent(context, ChatActivity::class.java))
//
//            }
//        })
//
//        viewManager = LinearLayoutManager(activity)
//        viewAdapter = ContactAdapter(callingListener1, callingListener2, context!!)
//        view!!.contactRecycler.apply {
//
//            layoutManager = viewManager
//            adapter = viewAdapter
//        }

    }

    override fun onDestroy() {
        task.cancel(true)
        super.onDestroy()
    }


}

