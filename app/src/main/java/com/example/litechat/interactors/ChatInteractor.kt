package com.example.litechat.interactors

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.MessageModel
import com.google.firebase.firestore.*
import java.lang.Double
import java.lang.NumberFormatException

import org.json.JSONObject
import org.json.JSONException
import com.nightonke.boommenu.Util.setText
import java.time.Instant

class ChatInteractor(p1: ChatContract.CPresenter) : ChatContract.CInteractor {

    private var database: FirebaseFirestore? = null
    private var chatPresenter: ChatContract.CPresenter? = null
    private var numeric = true
    var listener: ListenerRegistration? = null
    private var otherToken = ""
    private var document = ""
    private var lastUpdated = ""
    private val notificationKey = "key=AAAAVomZpgo:APA91bHu4Y7dcaeDiD6Uft-F91mqxxTRORAmz6VQQJWeqX3VUX5i_Nvnj8HdDuh6WB5ZO_puhqbfBtH-ylDzn6Lgm1yyBa7ccqSAtvGj9q4mIRQYi5NTd7fEFgk1BUSz18383i19dc1c"

    init {
        chatPresenter = p1
        database = FirebaseFirestore.getInstance()
    }

    override fun saveNewMessageToFirestore(messageModel: MessageModel, context: Context) {

        // sets new message to firestore

        try {
            val num = Double.parseDouble(AllChatDataModel.otherUserNumber)
        } catch (e: NumberFormatException) {
            numeric = false
        }

        database!!.collection("Chats").document(AllChatDataModel.documentPathId).collection("messages")
            .add(messageModel).addOnSuccessListener { res ->

            Log.d("Saala", "set on server ")
                if (numeric)
                {
                    database!!.collection("Users").document(AllChatDataModel.otherUserNumber).get().addOnSuccessListener {
                        otherToken = it.get("token").toString()
                        it.reference.collection("currentChats").document(AllChatDataModel.documentPathId).get().addOnSuccessListener {
                            lastUpdated = it.get("lastUpdated").toString()
                            document = AllChatDataModel.documentPathId
                            sendNotification(messageModel)
                        }
                    }
                }
                //TODO Add notification logic for groups
            Toast.makeText(context, "Message Sent Successfully", Toast.LENGTH_SHORT).show()
        }



        if (numeric) {
            // sets last updated to  both the users
            database!!.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
                .whereEqualTo("otherNumber", AllChatDataModel.otherUserNumber).get().addOnSuccessListener { documents ->

                    if (documents != null) {
                        for (doc in documents) {
                            doc.reference.update("lastUpdated", messageModel.sentOn)
                        }
                    }
                }

            database!!.collection("Users").document(AllChatDataModel.otherUserNumber).collection("currentChats")
                .whereEqualTo("otherNumber", AllChatDataModel.userNumberIdPM).get().addOnSuccessListener { documents ->

                    if (documents != null) {
                        for (doc in documents) {
                            doc.reference.update("lastUpdated", messageModel.sentOn)
                        }
                    }
                }


        }

        else
        {
            // to change last updted of all other members
            FirebaseFirestore.getInstance().collection("Users")
                .get().addOnSuccessListener { result ->

                    for (res in result) {
                        res.reference.collection("currentChats")
                            .whereEqualTo("chatDocumentId", AllChatDataModel.documentPathId).get()
                            .addOnSuccessListener { documents ->
                                Log.d("Tag5", "Enter query")

                                Log.d("Tag6", documents.toString())
                                for (doc in documents) {
                                    doc.reference.update("lastUpdated", messageModel.sentOn).addOnSuccessListener {

                                        Log.d("Tag3", "All personal chats deleted")
                                    }
                                }
                            }
                    }


                }
        }


    }

    private fun sendNotification(messageModel: MessageModel) {

        var body = JSONObject()
        try {
            body.put("to", otherToken)

            val message = JSONObject()
            message.put("body", messageModel.message)
            message.put("title", messageModel.sentBy)
            message.put("document" , document)
            message.put("lastUpdated" , lastUpdated)
            message.put("messageId" , Instant.now().epochSecond.toString())

            body.put("data", message)
        } catch (e: Exception) {
            Log.e("Notification error" , "Error in creating the body of the request")
        }

        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
            .addHeaders("Content-Type" , "application/json")
            .addHeaders("Authorization" , notificationKey)
            .addJSONObjectBody(body).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("Notification Response", response.toString())
                    try {
                        if (response.get("success") == 1) {
                            Log.d("Notification" , "Sucessfull")
                        } else {
                            Log.d("Notification" , "UnSucessfull")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Notification" , "Error occoured in the response")
                    }

                }

                override fun onError(anError: ANError) {
                    Log.e("Notification Error", anError.getErrorBody())
                    Log.e("Notification Error", anError.getErrorDetail())
                }
            })


    }


    override fun getNewMessageFromFirestore() {
        Log.d("Run1", "getNewMessageFromFirestore")


        // get personal chats and use caching of firestore
        var y = database!!.collection("Chats").document(AllChatDataModel.documentPathId)
            .collection("messages")

        listener = y.orderBy("sentOn")
            .addSnapshotListener(MetadataChanges.INCLUDE,
                EventListener<QuerySnapshot> { snap, e ->
                    AllChatDataModel.flag = !snap!!.metadata.hasPendingWrites()

                    if (e != null) {
                        Log.d("Error", "listen:error", e)
                        return@EventListener

                    }

                    for (dc in snap!!.documentChanges) {

                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var object1 = MessageModel()
                                object1.sentOn = dc.document["sentOn"].toString()
                                object1.sentBy = dc.document["sentBy"].toString()
                                object1.message = dc.document["message"].toString()

                                AllChatDataModel.allChatArrayListPersonalStatic.add(object1)

                                Log.d("FireStoreSnap", dc.document["message"].toString())
                                Log.d("Size", AllChatDataModel.allChatArrayListPersonalStatic.size.toString())
                            }

                        }
                    }


                    Log.d("FinalDebug17", "passNewMessagetoPrentercallled ${AllChatDataModel.flag}")

                    if (AllChatDataModel.flag) {
                        Log.d("FinalDebug16", "Inside Snapshot Listener for displaying new message")
                        AllChatDataModel.flag = false
                        chatPresenter!!.passNewMessageToPresenter()

                    }

                })

    }

    override fun removeListener() {

        Log.d("Tag4", "enter the method")
        FirebaseFirestore.getInstance().collection("Chats").document(AllChatDataModel.documentPathId)
            .collection("messages").get().addOnSuccessListener { documents ->

                if (documents.size() == 0) {
                    Log.d("Tag", "entered else condition")

                    FirebaseFirestore.getInstance().collection("Chats")
                        .document(AllChatDataModel.documentPathId).delete().addOnSuccessListener {

                            Log.d("Tag2", "Child deleted sucessfully")

                            FirebaseFirestore.getInstance().collection("Users")
                                .get().addOnSuccessListener { result ->

                                    for (res in result) {
                                        res.reference.collection("currentChats")
                                            .whereEqualTo("chatDocumentId", AllChatDataModel.documentPathId).get()
                                            .addOnSuccessListener { documents ->
                                                Log.d("Tag5", "Enter query")

                                                Log.d("Tag6", documents.toString())
                                                for (doc in documents) {
                                                    doc.reference.delete().addOnSuccessListener {

                                                        Log.d("Tag3", "All personal chats deleted")
                                                    }
                                                }
                                            }
                                    }
                                    AllChatDataModel.documentPathId = null

                                }
                        }
                }
            }

        listener!!.remove()
    }


}

