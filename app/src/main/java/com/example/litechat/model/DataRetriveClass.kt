package com.example.litechat.model

import android.content.Context
import android.util.Log
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.contactsRoom.User
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.QuerySnapshot

class DataRetriveClass : HomeActivityContract.Model{

    private val db = FirebaseFirestore.getInstance()
    override fun roomGetData(applicationContext: Context): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun roomSetData(applicationContext: Context, userList: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * This class should not be used currently as the structure of firestore is not yet finalized.
     */

    override fun getUserDataFromFirestore(number: String){
        db.collection("Users").document(number).get()
            .addOnSuccessListener {
                UserProfileData.UserName = it.getString("name")
                UserProfileData.UserNumber = it.getString("number")
            }
    }

    override fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter) {

        Log.d("FinalDebug3","vf")
        db.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentPersonalChats")
            .addSnapshotListener(
                MetadataChanges.INCLUDE,
                EventListener<QuerySnapshot>{ snap, e ->
                    Log.d("datau","1 : "+AllChatDataModel.flagPersonalChat.toString())
                    AllChatDataModel.flagPersonalChat = !snap!!.metadata.hasPendingWrites()
                    Log.d("data","2: "+AllChatDataModel.flagPersonalChat.toString())
                    if(e!=null){
                        Log.d("Error", "listen:error", e)
                        return@EventListener

                    }

                    for(dc in snap!!.documentChanges){

                        when(dc.type){
                            DocumentChange.Type.ADDED ->
                            {
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var objectChatPersonal :ChatObject= ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()

                                AllChatDataModel.personalChatList.add(objectChatPersonal)
                                Log.d("FinalDebug4","all upate${AllChatDataModel.personalChatList.size}")

                                Log.d("FireStoreSnap",  dc.document["otherNumber"].toString())
                                Log.d("HomeActivity","Size"+AllChatDataModel.personalChatList.size.toString())
                            }

                            DocumentChange.Type.MODIFIED ->
                            {
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var objectChatPersonal :ChatObject= ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()

                                AllChatDataModel.personalChatList.add(objectChatPersonal)
                                Log.d("FinalDebug4","all upate${AllChatDataModel.personalChatList.size}")

                                Log.d("FireStoreSnap",  dc.document["otherNumber"].toString())
                                Log.d("HomeActivity","Size"+AllChatDataModel.personalChatList.size.toString())
                            }

                        }
                    }


                    Log.d("Run3","passNewMessagetoPrentercallled")

                    if(AllChatDataModel.flagPersonalChat) {
                        AllChatDataModel.flagPersonalChat=false
                        Log.d("FinalDebug5", " persona presenter.sortPersonalChatList() ${AllChatDataModel.personalChatList.size}")
                        presenter.sortPersonalChatList()

                    }

                })
    }

}