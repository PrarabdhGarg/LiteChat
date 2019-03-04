package com.example.litechat.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ContactListModel
import kotlinx.android.synthetic.main.group_member_name.view.*


class GroupInfoAdapter(var memlist:ArrayList<String>,var context: Context): RecyclerView.Adapter<GroupInfoAdapter.GroupInfoHolder>() {
   class GroupInfoHolder(val view: View):RecyclerView.ViewHolder(view){
         var memName: TextView = view.findViewById(R.id.memberName);
        var img:ImageView=view.findViewById(R.id.imageViewGroupMembers)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GroupInfoHolder {
        Log.d("names","created")
        val view:View=LayoutInflater.from(p0.context).inflate(R.layout.group_member_name,p0,false)
        return GroupInfoHolder(view);

    }

    override fun getItemCount(): Int {
        Log.d("GroupInfo3",memlist.size.toString())
        return memlist.size
    }

    override fun onBindViewHolder(p0: GroupInfoHolder, p1: Int) {

        Log.d("names","binded")
        if(memlist.get(p1)!=AllChatDataModel.userNumberIdPM)
        p0.memName.text =(ContactListModel().roomGetName(context ,memlist.get(p1)))
        else
        p0.memName.text="You"

    }
}