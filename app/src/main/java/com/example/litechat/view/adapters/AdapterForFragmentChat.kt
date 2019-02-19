package com.example.litechat.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litechat.ListenerObjectTry
import com.example.litechat.R

class AdapterForFragmentChat(private var dataset :ArrayList<String>, private var context: Context,
                             private var listenerObjectTryImage: ListenerObjectTry,private var listenerObjectTryChat: ListenerObjectTry): RecyclerView.Adapter<AdapterForFragmentChat.MyViewHolder>() {

//    private lateinit var dataset: ArrayList<String>


    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        var textView: TextView =view.findViewById(R.id.chatName)
        var imageView: ImageView=view.findViewById(R.id.profileImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AdapterForFragmentChat.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_all_chat, parent,false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    } // create a new view


    override fun getItemCount(): Int {
        return dataset.size
           }

    override fun onBindViewHolder(holder: AdapterForFragmentChat.MyViewHolder, position: Int) {

        holder.textView.text = dataset[position]
        Glide.with(context).load(R.drawable.akshat).into(holder.imageView)

        holder.textView.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {
                  // change with number
               listenerObjectTryChat.listener!!.onDataRecieved(holder.textView!!.text.toString())
                // listener to send number for activity
            }
        })

        holder.imageView.setOnClickListener(object: View.OnClickListener{

            override fun onClick(v: View?) {

                // give number to launch profile
                listenerObjectTryImage.listener!!.onDataRecieved("contact number")
            }
        })
    }
}