package com.example.litechat.interactors

import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore

class FragmentChatInteractor():AllChatsContractFrag.CFInteractor {
    private  var database: FirebaseFirestore?=null
    private lateinit  var numberKeys :ArrayList<String>
    private lateinit var groupKeys:ArrayList<String>

    override fun getPersonalChats(): Array<Pair<String, MutableMap<String, Any>>> {
        lateinit var chats: Array<Pair<String, MutableMap<String, Any>>>
        database = FirebaseFirestore.getInstance()
        database!!.collection("Users").document(UserProfileData.UserNumber).get()
            .addOnSuccessListener { result ->


                var currentChats = result.get("currentChats") as Array<Pair<String, String>>


                for (i in 0 until currentChats.size) {
                    if (UserProfileData.UserNumber.toInt() > currentChats[i].first.toInt())
                        numberKeys.add(currentChats[i].first + UserProfileData.UserNumber)
                    else
                        numberKeys.add(UserProfileData.UserNumber + currentChats[i].first)

                }


                for (i in 0 until numberKeys.size) {

                    //lateinit var chatArray: Array<Pair<String,Array<Pair<String,String>>>>
                    database!!.collection("Chats").document(numberKeys[i]).get().addOnSuccessListener { result ->

                        chats[i] = Pair(numberKeys[i], result.data as MutableMap<String, Any>)
                    }
                }

            }
        return chats
    }



    override fun getGroupChats(): Array<Pair<String, MutableMap<String, Any>>> {
        lateinit var groupChats: Array<Pair<String,MutableMap<String,Any>>>
        database!!.collection("Users").document(UserProfileData.UserNumber).get()
            .addOnSuccessListener { result ->

                var currentGroupChats :Array<Pair<String , String>> = result.get("currentGroupChats") as Array<Pair<String, String>>

                for (i in 0 until currentGroupChats.size)
                {
                    groupKeys.add(currentGroupChats[i].first)

                }

                for(i in 0 until groupKeys.size){

                    database!!.collection("Chats").document(groupKeys[i]).get().addOnSuccessListener{result ->

                        groupChats[i] = Pair(groupKeys[i],result.data as MutableMap<String,Any>)
                    }
                }
            }

        return groupChats
    }

}