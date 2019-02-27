package com.example.litechat.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.ContactListData
import com.google.firebase.firestore.auth.User
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter(private val callListenerObject1: CallListenerObject, private val callListenerObject2: CallListenerObject, private val context: Context, private val contacts: ArrayList<com.example.litechat.model.contactsRoom.User>): RecyclerView.Adapter<ContactAdapter.ContactHolder>(){

    inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view){

        internal var names: TextView = view.findViewById(R.id.contactName)
        internal var img: CircleImageView = view.findViewById(R.id.contactImg)
        internal var call: ImageButton = view.findViewById(R.id.call)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.contacts_list, parent, false)
        return ContactHolder(view)
    }

    override fun getItemCount(): Int = ContactListData.contacts.size

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {

        holder.names.text = ContactListData.contacts[position].name
        Glide.with(context).load(R.drawable.suyash).into(holder.img)
        holder.names.setOnClickListener {

            ContactListData.userTapped = ContactListData.contacts[position]
            callListenerObject2.callListener!!.startCallIntent(contacts[position].mobileNumber)

        }
        holder.call.setOnClickListener {

            callListenerObject1.callListener!!.startCallIntent(contacts[position].mobileNumber)

        }
    }

}