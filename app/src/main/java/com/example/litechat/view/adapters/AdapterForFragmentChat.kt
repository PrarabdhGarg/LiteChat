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
import com.example.litechat.listeners.ListenerForFragmentChat
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListModel
import com.google.firebase.storage.FirebaseStorage
import java.lang.Error
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

        /**
         * Since recycler view works in a way such that it only recycles the information that is displayed in the views
         * of the recycler view, images of groups are also visible as currently we had not specified what image to overwrite
         * the previous image
         */

        holder.textView.text = ContactListModel().roomGetName(context , dataset[position].otherNumber)
        Log.d("QueryF",dataset[position].otherNumber+ " \n" +position.toString())
        try {
            var t = dataset[position].otherNumber.toDouble()
            Log.d("Firebase" , "Entered try")

            FirebaseStorage.getInstance().reference.child(dataset[position].otherNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
                Log.d("Firebse" , "Entered and retrivedd storage sucessfully ${it}")
                // onLoad Started ki wjah se profile image not being displayed in fragment chat
                try {
                    Glide.with(context).load(it.toString()).into(holder.imageView).clearOnDetach()
                }catch (e : Error)
                {
                    Log.d("Crash" , e.stackTrace.toString())
                }
            }



        }catch (e : Exception)
        {
            Log.d("Firebase" , "Entered catch \n ${e} with ${dataset[position].otherNumber}")
            Glide.with(context).load(R.drawable.prarabdh).into(holder.imageView).clearOnDetach()

            /**FirebaseStorage.getInstance().reference.child("Groups").child(AllChatDataModel.documentPathId).downloadUrl.addOnSuccessListener {
                Glide.with(context).load(it).into(holder.imageView).onLoadStarted(context.getDrawable(R.drawable.profile))
            }*/

        }
        Log.d("FinalDebug11","AllChatDataModel.personalChatList.size:${AllChatDataModel.personalChatList.size}\n${AllChatDataModel.personalChatList.contains(dataset[position])}")
        Log.d("TestNotif" , "position = ${position} \n LastSeen  = ${AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen} " +
                "\n  DocumentId = ${dataset[position].chatDocumentId} \n LastUpdated = ${AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastUpdated}")
        //AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < Instant.now().epochSecond.toString() &&
        if (AllChatDataModel.personalChatList.size!=0 && (AllChatDataModel.personalChatList.find { it.chatDocumentId==dataset[position].chatDocumentId }!=null) && AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < Instant.now().epochSecond.toString() && AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastUpdated)
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
                  // change with number
                /***
                 *
                 * change listner to pass chat id
                 */
                /*var t = ArrayList<ChatObject>()
                t.addAll(AllChatDataModel.personalChatList)
                var i = AllChatDataModel.personalChatList.indexOf(dataset[position])*/
                //Log.d("Debug13" , i.toString())
                /*if(i>=0)
                    t.removeAt(i)
                AllChatDataModel.personalChatList.addAll(t)*/
                Log.d("Debug13" , AllChatDataModel.personalChatList.size.toString())
                holder.greenDot.visibility = View.INVISIBLE
                Log.d("Dataa","first tigrme")
                Log.d("Persoo",holder.textView.text.toString())
                //AllChatDataModel.otherUserNumber=dataset[position].otherNumber
               listenerForFragmentChatChat.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated)

                // listener to send number for activity
            }
        })

        holder.imageView.setOnClickListener(object: View.OnClickListener{

            override fun onClick(v: View?) {
Log.d("GroupInfo2","documnetId from adapter${dataset[position].chatDocumentId}")
                // give number to launch profile
                listenerForFragmentChatImage.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated)
            }
        })
    }

    /*private fun searchContactName(number: String): String {

        val dbModel = ContactListModel()

        var namePassed = dbModel.roomGetName(context, number)

        if(namePassed.isEmpty()){
            return number
        }
        else{
            return namePassed
        }

    }*/
}