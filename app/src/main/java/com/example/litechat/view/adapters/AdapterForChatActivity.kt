package com.example.litechat.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.litechat.R
import com.example.litechat.model.MessageModel
import com.example.litechat.model.UserProfileData

class AdapterForChatActivity(private var number:String,private var dataset:ArrayList<MessageModel>): RecyclerView.Adapter<AdapterForChatActivity.MyViewHolder>() {

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var myName:TextView=view.findViewById(R.id.myName)
        var mymessage:TextView=view.findViewById(R.id.myMessage)
        var youName:TextView=view.findViewById(R.id.youName)
        var youMessage:TextView=view.findViewById(R.id.youMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AdapterForChatActivity.MyViewHolder {
        var view:View?=null
        //Here user no. is to be placed
        if(dataset[position].sentBy.equals("9826936889"))

        {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_chat_me, parent, false) as View}
       else{
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_chat_you, parent, false) as View
        }
        return MyViewHolder(view)
    } // create a new view


    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: AdapterForChatActivity.MyViewHolder, position: Int) {
        if(dataset[position].sentBy.equals("9826936889")) {
              holder.mymessage.setText(dataset[position].message)
              holder.myName.setText("9826936889")
        }
        else
        {
            holder.youMessage.setText(dataset[position].message)
            holder.youName.setText(dataset[position].sentBy)
        }
    }
}