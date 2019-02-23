package com.example.litechat.interactors

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.MessageModel
import com.example.litechat.model.UserDataModel
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.*
import java.util.concurrent.ArrayBlockingQueue

class ChatInteractor(p1:ChatContract.CPresenter) : ChatContract.CInteractor {

    private var database: FirebaseFirestore? = null
    private lateinit var numberKeys: ArrayList<String>
    private lateinit var groupKeys: ArrayList<String>
    private var chatPresenter:ChatContract.CPresenter?=null

    init {
        chatPresenter=p1
        database= FirebaseFirestore.getInstance()
    }

    override fun saveNewMessageToFirestore(messageModel: MessageModel,context: Context) {
        // created on is hardcoded
      /*  database!!.collection("Chats").whereEqualTo("createdOn", "1550871105").get().addOnSuccessListener { documents ->

            for (doc in documents) {
                doc.reference.collection("messages").add(messageModel)
            }
        }*/

        // get document id from somewhere
        database!!.collection("Chats").document("FK2fXmKyrmjVLhP4BfnA").collection("messages").add(messageModel).addOnSuccessListener { res->

            Toast.makeText(context,"Message Sent Successfully",Toast.LENGTH_LONG).show()
        }


    }
    override fun getNewMessageFromFirestore() {
        Log.d("Run1","getNewMessageFromFirestore")
        //prarbdh ka jugaad
    //    Log.d("Upp",AllChatDataModel.flag.toString())
      //  AllChatDataModel.flag=!(AllChatDataModel.flag)
     //   Log.d("Uppd",AllChatDataModel.flag.toString())

        //get document id from somewhere
        database!!.collection("Chats").document("FK2fXmKyrmjVLhP4BfnA").collection("messages").orderBy("sentOn")
            .addSnapshotListener(MetadataChanges.INCLUDE,
                EventListener<QuerySnapshot>{  snap , e ->
                    AllChatDataModel.flag = !snap!!.metadata.hasPendingWrites()

                    if(e!=null){
                        Log.d("Error", "listen:error", e)
                        return@EventListener

                    }
                 //   Log.d("Up", AllChatDataModel.flag.toString())
                //    AllChatDataModel.flag=!(AllChatDataModel.flag)
                   // Log.d("Upd", AllChatDataModel.flag.toString())
                    for(dc in snap!!.documentChanges){

                        when(dc.type){
                            DocumentChange.Type.ADDED ->
                            {
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var object1 :MessageModel= MessageModel()
                                object1.sentOn=dc.document["sentOn"].toString()
                                object1.sentBy=dc.document["sentBy"].toString()
                                object1.message=dc.document["message"].toString()

                                AllChatDataModel.allChatArrayListPersonalStatic.add(object1)

                                Log.d("FireStoreSnap",  dc.document["message"].toString())
                                Log.d("Size",AllChatDataModel.allChatArrayListPersonalStatic.size.toString())
                            }

                        }
                    }


                    Log.d("Run3","passNewMessagetoPrentercallled")
                   // AllChatDataModel.flag = false
                    if(AllChatDataModel.flag) {
                        AllChatDataModel.flag=false
                        chatPresenter!!.passNewMessageToPresenter()

                    }
                   // AllChatDataModel.flag=!(AllChatDataModel.flag)
                })
        /*database!!.collection("Chats")
            .whereEqualTo("createdOn", "1550871105")
            .addSnapshotListener(MetadataChanges.INCLUDE, EventListener<QuerySnapshot> { snapshots, e ->
                if (e != null) {
                    Log.d("Error", "listen:error", e)
                    return@EventListener
                }
                   Log.d("Changess", snapshots!!.metadata.toString())
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED ->
                        {
                            dc.document.reference.collection("messages").addSnapshotListener(EventListener<QuerySnapshot>{ querySnapshots,e ->
                                if(e!=null){
                                    Log.d("Error2", "listen:error", e)
                                    return@EventListener
                                }
                                for(daC in querySnapshots!!.documentChanges){
                                    when(daC.type){
                                        DocumentChange.Type.ADDED ->
                                            Log.d("Chhanges", "New city: " + daC.document["message"])
                                    }


                                }
                            })

                            *//* Log.d("Changes", "New city: " + dc.document.data)*//*
                        }

                       *//* DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: " + dc.document.data)
                        DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: " + dc.document.data)*//*
                    }
                }
            })*/

    }
}

