package com.example.litechat.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.litechat.R
import com.example.litechat.model.MessageModel

class AdapterForChatActivity(private var number:String,private var dataset :ArrayList<MessageModel>): RecyclerView.Adapter<AdapterForChatActivity.MyViewHolder>() {


    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var myName:TextView=view.findViewById(R.id.myName)
        var mymessage:TextView=view.findViewById(R.id.myMessage)
        var youName:TextView=view.findViewById(R.id.youName)
        var youMessage:TextView=view.findViewById(R.id.youMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AdapterForChatActivity.MyViewHolder {
        var view: View? = null
       if(dataset[position].sentBy.equals(number))
         view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_text_view, parent, false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view!!)
    } // create a new view


    override fun getItemCount(): Int {
        return  dataset.size

    }

    override fun onBindViewHolder(holder: AdapterForChatActivity.MyViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }
}