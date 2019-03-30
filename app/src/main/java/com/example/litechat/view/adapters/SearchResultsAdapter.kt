package com.example.litechat.view.adapters

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.listeners.ListenerForFragmentChat
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListModel
import com.google.firebase.storage.FirebaseStorage
import java.lang.Error
import java.lang.Exception


class SearchResultsAdapter(private var data:ArrayList<ChatObject>, private var context: Context,
                           private var Image: ListenerForFragmentChat, private var Name: ListenerForFragmentChat
):RecyclerView.Adapter<SearchResultsAdapter.SearchViewholder>() {
    class SearchViewholder(val view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView =view.findViewById(com.example.litechat.R.id.chatName)
        var imageView: ImageView =view.findViewById(com.example.litechat.R.id.profileImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SearchViewholder {
        val view: View=LayoutInflater.from(parent.context).inflate(R.layout.fragment_all_chat,parent,false)as View
        return SearchViewholder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchViewholder, p1: Int) {
        holder.textView.text = ContactListModel().roomGetName(context , data[p1].otherNumber)
        try {
            var t = data[p1].otherNumber.toDouble()
            FirebaseStorage.getInstance().reference.child(data[p1].otherNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
                try {
                    Glide.with(context).load(it.toString()).apply(RequestOptions().placeholder(context.getDrawable(R.drawable.profile))).
                        into(holder.imageView)
                }catch (e : Error)
                {
                    Log.d("Crash" , e.stackTrace.toString())
                }
            }
        }catch (e : Exception)
        {
            FirebaseStorage.getInstance().reference.child("groupimages").child(data[p1].chatDocumentId).downloadUrl.addOnSuccessListener {
                Glide.with(context)
                    .load(it)
                    .apply(RequestOptions().placeholder(context.getDrawable(R.drawable.ic_group)))
                    .into(holder.imageView)

            }
        }
      holder.textView.setOnClickListener(object : View.OnClickListener{
          override fun onClick(v: View?) {
              Name.listener!!.onDataRecieved(data[p1].otherNumber,data[p1].chatDocumentId,data[p1].lastUpdated)
          }

      })
      holder.imageView.setOnClickListener(object : View.OnClickListener{
          override fun onClick(v1: View?) {
              Image.listener!!.onDataRecieved(data[p1].otherNumber,data[p1].chatDocumentId,data[p1].lastUpdated)
          }

      })
    }


}