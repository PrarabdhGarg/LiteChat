package com.example.litechat.view.adapters

import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.litechat.R
import kotlinx.android.synthetic.main.group_member_name.view.*

class GroupInfoAdapter(var memlist:ArrayList<String>): RecyclerView.Adapter<GroupInfoAdapter.GroupInfoHolder>() {
   class GroupInfoHolder(val view: View):RecyclerView.ViewHolder(view){
         var memName: TextView = view.findViewById(R.id.memberName);
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GroupInfoHolder {
        val view:View=LayoutInflater.from(p0.context).inflate(R.layout.group_member_name,p0,false)
        return GroupInfoHolder(view);
    }

    override fun getItemCount(): Int {
        return memlist.size
    }

    override fun onBindViewHolder(p0: GroupInfoHolder, p1: Int) {
       p0.memName.setText(memlist.get(p1))
    }
}