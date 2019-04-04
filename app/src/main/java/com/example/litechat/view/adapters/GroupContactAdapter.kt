package com.example.litechat.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.model.ContactListData
import com.example.litechat.model.UserProfileData
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Error

class GroupContactAdapter(var context: Context): RecyclerView.Adapter<GroupContactAdapter.GroupContactHolder>() {
    inner class GroupContactHolder(view: View) : RecyclerView.ViewHolder(view){

        internal var names: TextView = view.findViewById(R.id.contactName)
        internal var img: CircleImageView = view.findViewById(R.id.contactImg)
        internal var check: CircleImageView=view.findViewById(R.id.check)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupContactAdapter.GroupContactHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.group_contacts_list, parent, false)
        return GroupContactHolder(view)
    }

    override fun getItemCount(): Int = ContactListData.contacts.size

    override fun onBindViewHolder(holder: GroupContactAdapter.GroupContactHolder, position: Int) {

        holder.names.text = ContactListData.contacts[position].name
        FirebaseStorage.getInstance().reference.child(ContactListData.contacts[position].mobileNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
            try {
                Glide.with(context).load(it.toString()).apply(RequestOptions().placeholder(context.getDrawable(R.drawable.profile))).
                    into(holder.img)
            }catch (e : Error)
            {
                Log.d("Crash" , e.stackTrace.toString())
            }
        }
        holder.names.setOnClickListener{
            Log.d("check","exec")
            if(holder.check.visibility == View.INVISIBLE){

                if (ContactListData.contacts[position].mobileNumber==UserProfileData.UserNumber) {
                   Toast.makeText(context,"You are already in the group",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    holder.check.visibility = View.VISIBLE
                    ContactListData.groupContacts.add(ContactListData.contacts[position])
                    Log.d("check",ContactListData.groupContacts.toString())
                }
            }
            else{
                holder.check.visibility = View.INVISIBLE
                Glide.with(context).load(R.drawable.profile).into(holder.img)
                ContactListData.groupContacts.remove(ContactListData.contacts[position])
                Log.d("check",ContactListData.groupContacts.toString())
            }
        }
    }


}