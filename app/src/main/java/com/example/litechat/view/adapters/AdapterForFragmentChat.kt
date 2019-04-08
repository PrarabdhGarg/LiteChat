package com.example.litechat.view.adapters

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.listeners.ListenerForFragmentChat
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListModel
import java.lang.Exception
import java.time.Instant


class AdapterForFragmentChat(private var dataset :ArrayList<ChatObject>, private var context: Context,
                             private var listenerForFragmentChatImage: ListenerForFragmentChat, private var listenerForFragmentChatChat: ListenerForFragmentChat
): RecyclerView.Adapter<AdapterForFragmentChat.MyViewHolder>() {


    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        var textView: TextView =view.findViewById(com.example.litechat.R.id.chatName)
        var imageView: ImageView=view.findViewById(com.example.litechat.R.id.profileImage)
        var greenDot: ImageView=view.findViewById(com.example.litechat.R.id.imageViewGreenDot)

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AdapterForFragmentChat.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.litechat.R.layout.fragment_all_chat, parent,false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    } // create a new view

    override fun getItemCount(): Int = dataset.size

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: AdapterForFragmentChat.MyViewHolder, position: Int) {
        /*
         * Since recycler view works in a way such that it only recycles the information that is displayed in the views
         * of the recycler view, images of groups are also visible as currently we had not specified what image to overwrite
         * the previous image
         */
        holder.textView.isClickable = true
        holder.textView.text = ContactListModel().roomGetName(context , dataset[position].otherNumber)
        Glide.with(context).clear(holder.imageView)
        Log.d("QueryF",dataset[position].otherNumber+ " \n" +position.toString())
        try {
            var t = dataset[position].otherNumber.toDouble()
            Log.d("RoomImage11" , "Entered try ${dataset[position].otherNumber}")

                 if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                Glide.with(context)
                    .apply { RequestOptions().placeholder(R.drawable.profile) }
                    .load((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL)
                    .into(holder.imageView)
               Log.d("RoomImage13", "if of personal + ${dataset[position].otherNumber}")
            }
            else
            {
                Glide.with(context).load(R.drawable.profile).into(holder.imageView)
                Log.d("RoomImage14", "else of personal + ${dataset[position].otherNumber}")
            }
            /*Glide.with(context).load("gs://litechat-3960c.appspot.com/${dataset[position].otherNumber}/ProfileImage").apply(RequestOptions().placeholder(context.getDrawable(R.drawable.profile))).
                into(holder.imageView)*/
        }catch (e : Exception)
        {
            Log.e("RoomImage15" , "Entered catch \n ${e} with ${dataset[position].otherNumber}")

            if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                Glide.with(context)
                    .apply { RequestOptions().placeholder(R.drawable.profile) }
                    .load((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL)
                    .into(holder.imageView)
                Log.d("RoomImage16", "if of group + ${dataset[position].otherNumber}")
            }
                else
            {
                Glide.with(context).load(R.drawable.ic_group).into(holder.imageView)
                Log.d("RoomImage17", "else of group + ${dataset[position].otherNumber}")

            }
            /*Glide.with(context).load("gs://litechat-3960c.appspot.com/groupimages/${dataset[position].chatDocumentId}").apply(RequestOptions().placeholder(context.getDrawable(R.drawable.profile))).
                into(holder.imageView)*/
        }

        Log.d("FinalDebug11","AllChatDataModel.personalChatList.size:" +
                "${AllChatDataModel.personalChatList.size}\n${AllChatDataModel.personalChatList.contains(dataset[position])}")


        //AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < Instant.now().epochSecond.toString() &&

        if (AllChatDataModel.personalChatList.size!=0 &&
                (AllChatDataModel.personalChatList.find { it.chatDocumentId==dataset[position].chatDocumentId }!=null)
                && AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < Instant.now().epochSecond.toString() &&
                AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastUpdated)
        {
            holder.greenDot.visibility = View.VISIBLE
        }
        else
        {
            holder.greenDot.visibility = View.INVISIBLE
        }

        holder.textView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                Log.d("AllChatNumber" , AllChatDataModel.userNumberIdPM)

                Log.d("Debug13" , AllChatDataModel.personalChatList.size.toString())
                Log.d("Data","first time")
                Log.d("Person",holder.textView.text.toString())
               var urlToPass = " "
                if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                    urlToPass = (AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL
                    Log.d("RoomImage21", "if of onclick + ${dataset[position].otherNumber}")
                }
                else
                {

                    Log.d("RoomImage14", "else of onclick+ ${dataset[position].otherNumber}")
                }
               listenerForFragmentChatChat.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated, urlToPass)
                // listener to send number for activity
            }
        })

        holder.imageView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?)
            {
                Log.d("GroupInfo2","documentId from adapter${dataset[position].chatDocumentId}")
                // give number to launch profile
                var urlToPass =" "
                if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null)
                {
                    urlToPass = (AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL
                    Log.d("RoomImage21", "if of onclick + ${dataset[position].otherNumber}")
                }
                else
                {
                    Log.d("RoomImage14", "else of onclick+ ${dataset[position].otherNumber}")
                }
                listenerForFragmentChatImage.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated,urlToPass)
            }
        })
    }

}