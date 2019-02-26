package com.example.litechat.view.adapters

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litechat.ListenerObjectTry
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.DataChatModel

class AdapterForFragmentChat(private var dataset :ArrayList<ChatObject>, private var context: Context,
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: AdapterForFragmentChat.MyViewHolder, position: Int) {

        holder.textView.text = dataset[position].otherNumber
        Log.d("QueryF",dataset[position].otherNumber+ " \n" +position.toString())
        Glide.with(context).load(R.drawable.profile).into(holder.imageView)
        Log.d("FinalDebug11","AllChatDataModel.personalChatList.size:${AllChatDataModel.personalChatList.size}\n${AllChatDataModel.personalChatList.contains(dataset[position])}")
        if (AllChatDataModel.personalChatList.size!=0 && (AllChatDataModel.personalChatList.find { it.chatDocumentId==dataset[position].chatDocumentId }!=null))
        {
            Log.d("FinalDebuf12","AllChatDataModel.personalChatList.size:${AllChatDataModel.personalChatList.size}\n${AllChatDataModel.personalChatList.contains(dataset[position])}")
            Glide.with(context).load(R.drawable.ic_checked).into(holder.imageView)
        }

        holder.textView.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {
                Log.d("AllChatNumber" , AllChatDataModel.userNumberIdPM)
                  // change with number
                /***
                 *
                 * change listner to pass chat id
                 */
                var t = ArrayList<ChatObject>()
                t.addAll(AllChatDataModel.personalChatList)
                var i = AllChatDataModel.personalChatList.indexOf(dataset[position])
                Log.d("Debug13" , i.toString())
                if(i>=0)
                    t.removeAt(i)
                AllChatDataModel.personalChatList.addAll(t)
                Log.d("Debug13" , AllChatDataModel.personalChatList.size.toString())
                Glide.with(context).load(R.drawable.profile).into(holder.imageView)
                Log.d("Dataa","first tigrme")
                Log.d("Persoo",holder.textView!!.text.toString())
                AllChatDataModel.otherUserNumber=dataset[position].otherNumber
               listenerObjectTryChat.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated)

                // listener to send number for activity
            }
        })

        holder.imageView.setOnClickListener(object: View.OnClickListener{

            override fun onClick(v: View?) {

                // give number to launch profile
                listenerObjectTryImage.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated)
            }
        })
    }
}