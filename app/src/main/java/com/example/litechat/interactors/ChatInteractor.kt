package com.example.litechat.interactors

import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.UserDataModel
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ArrayBlockingQueue

class ChatInteractor : ChatContract.CInteractor {

    private var database: FirebaseFirestore? = null
    private lateinit var numberKeys: ArrayList<String>
    private lateinit var groupKeys: ArrayList<String>
}
//
//    override fun getPersonalChats(): Array<Pair<String, MutableMap<String, Any>>> {
//
//
//    }

  /* private  var database: FirebaseFirestore?=null
    private lateinit  var numberKeys :ArrayList<String>
private lateinit var groupKeys:ArrayList<String>

    override fun getPersonalChats(): Array<Pair<String, MutableMap<String, Any>>> {
>>>>>>> 2616dcab4e0c95f8a07bba1668c4be926b2ac56a
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

<<<<<<< HEAD
=======

>>>>>>> 2616dcab4e0c95f8a07bba1668c4be926b2ac56a
                for (i in 0 until numberKeys.size) {

                    //lateinit var chatArray: Array<Pair<String,Array<Pair<String,String>>>>
                    database!!.collection("Chats").document(numberKeys[i]).get().addOnSuccessListener { result ->

                        chats[i] = Pair(numberKeys[i], result.data as MutableMap<String, Any>)
                    }
                }

            }
        return chats
    }



<<<<<<< HEAD
        return chats


    }



    override fun getGroupChats(): Array<Pair<String, MutableMap<String, Any>>> {

        lateinit var groupChats: Array<Pair<String, MutableMap<String, Any>>>

=======
    override fun getGroupChats(): Array<Pair<String, MutableMap<String, Any>>> {
        lateinit var groupChats: Array<Pair<String,MutableMap<String,Any>>>
>>>>>>> 2616dcab4e0c95f8a07bba1668c4be926b2ac56a
        database!!.collection("Users").document(UserProfileData.UserNumber).get()
            .addOnSuccessListener { result ->

                var currentGroupChats: Array<Pair<String, String>> =
                    result.get("currentGroupChats") as Array<Pair<String, String>>

                for (i in 0 until currentGroupChats.size) {
                    groupKeys.add(currentGroupChats[i].first)

                }

                for (i in 0 until groupKeys.size) {

<<<<<<< HEAD
                    database!!.collection("Chats").document(groupKeys[i]).get().addOnSuccessListener { result ->
=======
                    database!!.collection("Chats").document(groupKeys[i]).get().addOnSuccessListener{result ->
>>>>>>> 2616dcab4e0c95f8a07bba1668c4be926b2ac56a

                        groupChats[i] = Pair(groupKeys[i], result.data as MutableMap<String, Any>)
                    }
                }
            }
        return groupChats

<<<<<<< HEAD
    }


}
=======
     return groupChats
    }
*/
