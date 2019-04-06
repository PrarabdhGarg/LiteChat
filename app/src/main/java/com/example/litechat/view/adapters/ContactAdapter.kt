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
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ContactListData
import com.example.litechat.model.UserProfileData
import com.google.firebase.storage.FirebaseStorage
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
            Glide.with(context).clear(holder.img)
            holder.names.text = ContactListData.contacts[position].name

            FirebaseStorage.getInstance().reference.child(ContactListData.contacts[position].mobileNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {

                Glide.with(context).load(it.toString()).apply{RequestOptions().placeholder(R.drawable.profile)}.into(holder.img)
            }
           /* if(ContactListData.contacts[position].mobileNumber==UserProfileData.UserNumber)
            {
                Glide.with(context).load()
            }*/

       /* if (ContactListData.contacts[position].mobileNumber!=UserProfileData.UserNumber) {
            try {
                if((AllChatDataModel.urlList
                        .find { it.chatDocumentId == AllChatDataModel.personalChatList
                            .find { it.otherNumber==ContactListData.contacts[position].mobileNumber }!!.chatDocumentId })!= null)
                    Glide.with(context)
                        .apply { RequestOptions().placeholder(R.drawable.profile)}
                        .load((AllChatDataModel.urlList
                            .find { it.chatDocumentId == AllChatDataModel.personalChatList
                                .find { it.otherNumber==ContactListData.contacts[position].mobileNumber }!!.chatDocumentId })!!.URL).into(holder.img)

                else
                    Glide.with(context).load(R.drawable.profile).into(holder.img)
            } catch (e: Exception) {
                Log.d("RoomContact","$e")
            }
        } else
        {
          Glide.with(context).load(UserProfileData.UserImage).apply(RequestOptions().placeholder(R.drawable.profile)).into(holder.img)
        }
*/

        holder.names.setOnClickListener {


            ContactListData.userTapped = ContactListData.contacts[position]
            callListenerObject2.callListener!!.startCallIntent(contacts[position].mobileNumber)

        }
        holder.call.setOnClickListener {

            callListenerObject1.callListener!!.startCallIntent(contacts[position].mobileNumber)

        }
    }

}