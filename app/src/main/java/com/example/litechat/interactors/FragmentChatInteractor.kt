package com.example.litechat.interactors

import android.app.Notification
import android.util.Log
import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.model.ChatModelK
import com.example.litechat.model.DataChatModel
import com.example.litechat.model.UserProfileData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FragmentChatInteractor(p1: AllChatsContractFrag.CFPresenter) : AllChatsContractFrag.CFInteractor {

    private var database: FirebaseFirestore? = null
    private lateinit var numberKeys: ArrayList<String>
    private lateinit var groupKeys: ArrayList<String>
    private lateinit var p2: AllChatsContractFrag.CFPresenter

    init {
        p2 = p1
    }

    override fun getPersonalChats()/*: ArrayList<ChatModelK> */ {

        var chatModel = ArrayList<ChatModelK>()

        lateinit var chats: Array<Pair<String, MutableMap<String, Any>>>
         var finalNumberKeys= ArrayList<String>()
        lateinit var current: ArrayList<String>
        var arrayChat = ArrayList<Pair<String,String?>>()
        database = FirebaseFirestore.getInstance()


        database!!.collection("Users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                for (document in task.result!!) {

                    var currentChats = document.data
                    var dd = currentChats.get("number") as String?
                    if (dd.equals("9826936889")) {
                        current = currentChats.get("currentChats") as ArrayList<String>
                        Log.d("Final", current[0] + current[1])

                    }
                }

                    for (i in 0 until current.size) {
                        if (9826936889 > current[i].toLong()) {
                            finalNumberKeys.add(current[i] +"9826936889")

                        } else {

                            finalNumberKeys.add("9826936889" + finalNumberKeys[i])

                        }
                    }

                    Log.d("Fin",UserProfileData.UserNumber.toString())

                   /* for (i in 0 until finalNumberKeys.size) {*/

                        //lateinit var chatArray: Array<Pair<String,Array<Pair<String,String>>>>
                        database!!.collection("Chats").get().addOnCompleteListener { document ->
                              if(document.isSuccessful){

                                  for(doc in document.result!!){

                                      var personalChat = doc.data as HashMap<String,String>

                                      var sorted = personalChat.toSortedMap(compareBy { it })
                                      Log.d("k1",personalChat.toString())
                                      Log.d("k1",sorted.toString())
                                      var arrayChit: MutableCollection<String> = sorted.values


                                      for (i in 0 until arrayChit.size){
                                          var message = arrayChit.elementAt(i).indexOf('$',0,false)
                                          var name = arrayChit.elementAt(i).substring(0,message)
                                          var mess = arrayChit.elementAt(i).substring(message+1)
                                          Log.d("FinalMessage",name+"\n"+mess)
                                      }
                                     /*
                                      arrayChat.add(Pair(personalChat.keys.first(), personalChat[personalChat.keys.first()]))
                                      Log.d("Finaaa",arrayChat.toString()+arrayChat.get(0).second)
                                      var message = arrayChat[0].second!!.indexOf('$',0,false)
                                            var name = arrayChat[0].second!!.substring(0,message)
                                            var mess = arrayChat[0].second!!.substring(message+1)*/
                                    //  Log.d("FinalMessage",name+"\n"+mess)

                                  }
                              //}

                        }
                    //}




                }
            }
        }


        /* val docRef = database.collection("Users").document(UserProfileData.)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.data)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }*/
        // [END get_document]
       /* database!!.collection("Users").document(UserProfileData.UserNumber).get()
            .addOnSuccessListener { result ->


                var currentChats = result.get("currentChats") as Array<Pair<String, String>>

                for (i in 0 until currentChats.size) {
                    if (UserProfileData.UserNumber.toInt() > currentChats[i].first.toInt()) {
                        numberKeys.add(currentChats[i].first + UserProfileData.UserNumber)
                        chatModel[i].currentChats = android.util.Pair(currentChats[i].first, currentChats[i].second)
                    } else {

                        numberKeys.add(UserProfileData.UserNumber + currentChats[i].first)
                        chatModel[i].currentChats = android.util.Pair(currentChats[i].first, currentChats[i].second)
                    }
                }


                for (i in 0 until numberKeys.size) {

                    //lateinit var chatArray: Array<Pair<String,Array<Pair<String,String>>>>
                    database!!.collection("Chats").document(numberKeys[i]).get().addOnSuccessListener { result ->

                        chats[i] = Pair(numberKeys[i], result.data as MutableMap<String, Any>)
                        chatModel[i].chats = chats[i]
                    }
                }

                DataChatModel.personalChatModelArray = chatModel
                p2.personalChatsDataRecieved()

            }*/


        /*   return chatModel*/
    }


    override fun getGroupChats()/*: ArrayList<ChatModelK> */ {

       /* lateinit var groupChats: Array<Pair<String, MutableMap<String, Any>>>
        var groupChatModel = ArrayList<ChatModelK>()

        database!!.collection("Users").document(UserProfileData.UserNumber).get()
            .addOnSuccessListener { result ->

                var currentGroupChats: Array<Pair<String, String>> =
                    result.get("currentGroupChats") as Array<Pair<String, String>>

                for (i in 0 until currentGroupChats.size) {
                    groupKeys.add(currentGroupChats[i].first)

                    groupChatModel[i].currentChats =
                        android.util.Pair(currentGroupChats[i].first, currentGroupChats[i].second)


                }

                for (i in 0 until groupKeys.size) {

                    database!!.collection("Chats").document(groupKeys[i]).get().addOnSuccessListener { result ->

                        groupChats[i] = Pair(groupKeys[i], result.data as MutableMap<String, Any>)


                        groupChatModel[i].chats = groupChats[i]


                    }
                }

                DataChatModel.groupChatModelArray = groupChatModel
            }*/

        /*return groupChats*/
    }

}