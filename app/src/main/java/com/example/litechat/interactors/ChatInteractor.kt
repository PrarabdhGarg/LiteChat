package com.example.litechat.interactors

import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.UserDataModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ArrayBlockingQueue

class ChatInteractor : ChatContract.CInteractor {
   private  var database: FirebaseFirestore?=null
    override fun getAllCurrentChatNames(number: String) {

        database= FirebaseFirestore.getInstance()
        var ref1= database!!.collection("Users").document().get().addOnSuccessListener { result ->

            lateinit var array :MutableList<Pair<String,String>>
            //array.add(9,Pair("f","d"))
            val map = result.getData()
            var i = 0;
            for (entry in map!!.entries) {
                array.add(i, entry.value as Pair<String, String>)
                i++
            }
////

        }




    }
    override fun getMessage(number: String) {

        database= FirebaseFirestore.getInstance()
        // add cache listener
        var ref1= database!!.collection("Chats").document(number).get().addOnSuccessListener {result ->

           lateinit var array :MutableList<Pair<String,String>>
            //array.add(9,Pair("f","d"))
            val map = result.getData()
            var i = 0;
            for (entry in map!!.entries) {
                array.add(i, entry.value as Pair<String, String>)
                i++
            }
////

        }


      }



}