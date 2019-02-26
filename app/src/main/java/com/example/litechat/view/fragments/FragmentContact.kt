package com.example.litechat.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litechat.R
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.ContactListData
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.ContactFragPresenter
import com.example.litechat.view.activities.ChatActivity
import com.example.litechat.view.adapters.ContactAdapter
import kotlinx.android.synthetic.main.fragment_contact.view.*
import java.util.ArrayList

class FragmentContact: Fragment(), ContactFragContract.View{

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var contactPresenter: ContactFragPresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        contactPresenter = ContactFragPresenter(this,context!!)

        if (contactPresenter.passUserList().isEmpty()) {

            contactPresenter.getContacts()

        }


        ContactListData.contacts = contactPresenter.passUserList() as ArrayList<User>

        return view
    }

    override fun onStart() {
        super.onStart()

        val callingListener1 = CallListenerObject()
        callingListener1.setListener(object : CallListenerObject.CallListener{

            override fun startCallIntent(number: String) {

                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            }
        })

        val callingListener2 = CallListenerObject()
        callingListener2.setListener(object : CallListenerObject.CallListener{

            override fun startCallIntent(number: String) {

                startActivity(Intent(context, ChatActivity::class.java))

            }
        })

        viewManager = LinearLayoutManager(activity)
        viewAdapter = ContactAdapter(callingListener1, callingListener2, context!!)
        view!!.contactRecycler.apply {

            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    }
