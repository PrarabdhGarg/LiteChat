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

class DeveloperAdapter(private var fnamesAndImages: List<Pair<String, Int>>, private var context: Context): RecyclerView.Adapter<DeveloperAdapter.DeveloperHolder>() {

    inner class DeveloperHolder(val view: View) : RecyclerView.ViewHolder(view) {

        internal var names: TextView = view.findViewById(R.id.name)
        internal var img: CircleImageView = view.findViewById(R.id.img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.developers_list, parent, false)
        return DeveloperHolder(view)
    }

    override fun getItemCount(): Int = fnamesAndImages.size

    override fun onBindViewHolder(holder: DeveloperHolder, position: Int) {

        val (fname, fimage) = fnamesAndImages[position]
        holder.names.text = fname
        Glide.with(context).load(fimage).into(holder.img)

    }


}