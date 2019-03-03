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
import android.view.WindowManager
import com.example.litechat.R
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.listeners.BoomListener
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListData
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.ContactFragPresenter
import com.example.litechat.view.activities.*
import com.example.litechat.view.adapters.ContactAdapter
import com.google.firebase.auth.FirebaseAuth
import com.nightonke.boommenu.BoomButtons.HamButton
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_contact.view.*
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.util.ArrayList

class FragmentContact : Fragment(), ContactFragContract.View {

    lateinit var contactPresenter: ContactFragPresenter
    lateinit var activitySet: FragmentActivity
    lateinit var task : AsyncTask<Void , Void , Void>
    private var dataSet = ArrayList<User>()
    lateinit var adapterListener: BoomListener
    private var flag : Boolean? = null

    override fun startChatActivity(chatObject: ChatObject) {

        var intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("documentPathId",chatObject.chatDocumentId)
        intent.putExtra("string", chatObject.otherNumber)
        intent.putExtra("lastUpdated",chatObject.lastUpdated)
        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        startActivity(intent)
    }


    inner class getingContacts(val contactPresenter: ContactFragPresenter, val adapter: BoomListener) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {

                contactPresenter.getContacts(adapter)
                ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>
            return null
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)


        contactPresenter = ContactFragPresenter(this, context!!)

        ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>




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

                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                contactPresenter.startNewChatFromContact(number)
                Log.d("Context",context.toString())

            }
        })

        adapterListener = BoomListener()
        adapterListener.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {

                ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>
                Log.d("ContactThread","In Listener")
                dataSet.clear()
                dataSet.addAll(ContactListData.contacts)
                Log.d("ContactThread","${dataSet.size}")
                contactLoader.visibility = View.GONE
                view.contactRecycler.adapter!!.notifyDataSetChanged()
            }

        })

        //viewAdapter = ContactAdapter(callingListener1, callingListener2, context!!, dataSet)
        dataSet.addAll(ContactListData.contacts)
        if(dataSet.size!=0)
        {
            view.contactLoader.visibility = View.GONE
        }
        view.contactRecycler.adapter = ContactAdapter(callingListener1, callingListener2, context!!, dataSet)


        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flag = true
        Log.e("ViewPager" , "OnCreateCalled")

    }
    override fun onStart() {
        super.onStart()
        Log.e("ViewPager" , "OnStartCalled")
        activitySet = activity!!
        task = getingContacts(contactPresenter, adapterListener)
        if ((ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) && (task.status != AsyncTask.Status.RUNNING) && flag!!){
            flag = false
            task.execute()
        }

    }

    override fun onResume() {
        Log.e("ViewPager" , "OnResumeCalled")
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.e("ViewPager" , "OnActivityCreatedCalled")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onPause() {
        Log.e("ViewPager" , "OnActivityCreatedCalled")
        super.onPause()
    }


}

