package com.example.litechat.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.litechat.R

class PersonalChatAdapter( dataset :ArrayList<String>): RecyclerView.Adapter<PersonalChatAdapter.MyViewHolder>() {

private lateinit var dataset: ArrayList<String>
    class MyViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PersonalChatAdapter.MyViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_text_view, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(textView)
    } // create a new view


    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PersonalChatAdapter.MyViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder.textView.text = dataset[position]
    }
}