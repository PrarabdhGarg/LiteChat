package com.example.litechat.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litechat.R
import de.hdodenhof.circleimageview.CircleImageView

class StatusAdapter(private var context : Context , var map : ArrayList<Pair<String , String>>) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.status_items, parent, false)
        return StatusViewHolder(view)
    }

    override fun getItemCount(): Int  = map.size

    override fun onBindViewHolder(viewHolder: StatusViewHolder, p1: Int) {
        viewHolder.currentActivities.text = map[p1].first
        Glide.with(context).load(map[p1].second).into(viewHolder.statusImages)
    }

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        internal var currentActivities: TextView = itemView.findViewById(R.id.ActivityTextView)
        internal var statusImages: CircleImageView = itemView.findViewById(R.id.statusImage)
    }
}