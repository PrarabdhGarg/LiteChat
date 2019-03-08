package com.example.litechat.model

import android.content.Context
import android.provider.DocumentsContract
import android.util.Log
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.presenter.StatusFragmentPresenter
import com.google.firebase.firestore.*

import android.widget.Toast

import com.example.litechat.presenter.HomeActivityPresenter

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.QuerySnapshot

class DataRetriveClass : HomeActivityContract.Model{

    private val db = FirebaseFirestore.getInstance()
    /**
     * This class should not be used currently as the structure of firestore is not yet finalized.
     */

    override fun getUserDataFromFirestore(number: String){
        db.collection("Users").document(number).get()
            .addOnSuccessListener {
                UserProfileData.UserName = it.getString("name")
                UserProfileData.UserNumber = it.getString("number")
                UserProfileData.UserCurrentActivity = it.get("currentActivity").toString()
                UserProfileData.UserAbout = it.getString("about").toString()
                UserProfileData.UserImage = it.getString("image").toString()
                UserProfileData.UserProfileImage = it.getString("profileImage").toString()
                Log.d("UserData" , "Data Retrieved class successfully called")
            }
    }

    override fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter) {

        Log.d("FinalDebug3","vf")
        db.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
            .addSnapshotListener(
                MetadataChanges.INCLUDE,
                EventListener<QuerySnapshot>{ snap, e ->
                    Log.d("datau","1 : "+AllChatDataModel.flagPersonalChat.toString())
                    AllChatDataModel.flagPersonalChat = !snap!!.metadata.hasPendingWrites()
                    Log.d("data","2: "+AllChatDataModel.flagPersonalChat.toString())
                    if(e != null){
                        Log.d("Error", "listen:error", e)
                        return@EventListener
                    }

                    for(dc in snap!!.documentChanges){

                        when(dc.type){
                            DocumentChange.Type.ADDED ->
                            {
                                Log.d("FinalDebug18" , "Added Called With ${dc} \n")
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var objectChatPersonal = ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()
                                objectChatPersonal.lastSeen = dc.document["lastSeen"].toString()
                                if(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId } == null)
                                {
                                    AllChatDataModel.personalChatList.add(objectChatPersonal)
                                }
                                Log.d("FinalDebug4","all upate in added with ${AllChatDataModel.personalChatList.size}")
                                Log.d("FireStoreSnap",  dc.document["otherNumber"].toString())
                                Log.d("HomeActivity","Size"+AllChatDataModel.personalChatList.size.toString())
                            }

                            DocumentChange.Type.MODIFIED ->
                            {
                                Log.d("FinalDebug19" , "Modified called with ${dc} \n")
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var objectChatPersonal :ChatObject= ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()
                                objectChatPersonal.lastSeen = dc.document["lastSeen"].toString()

                                if(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId } == null)
                                {
                                    AllChatDataModel.personalChatList.add(objectChatPersonal)
                                }
                                else
                                {
                                    AllChatDataModel.personalChatList.set(AllChatDataModel.personalChatList.indexOf(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId }) , objectChatPersonal)
                                }
                                Log.d("FinalDebug4","all upate${AllChatDataModel.personalChatList.size}")
                                Log.d("FireStoreSnap",  dc.document["otherNumber"].toString())
                                Log.d("HomeActivity","Size"+AllChatDataModel.personalChatList.size.toString())
                            }

                            DocumentChange.Type.REMOVED ->
                            {
                                Log.d("BeforeDeletion" , "Data : ${dc.document.data} \n Size : ${AllChatDataModel.personalChatList.size}}")
                                var objectChatPersonal :ChatObject= ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()
                                objectChatPersonal.lastSeen = dc.document["lastSeen"].toString()
                                AllChatDataModel.personalChatList.remove(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId })
                               // presenter.sortPersonalChatList()
                                Log.d("AfterDeletion" , "Size : ${AllChatDataModel.personalChatList.size} \n Object : ${objectChatPersonal.otherNumber}")
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


    override fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter, context: Context) {
        var maps = ArrayList<Pair<String , String>>()
        var model = ContactListModel()
        FirebaseFirestore.getInstance().collection("Users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.id == UserProfileData.UserNumber)
                        continue
                    maps.add(Pair(model.roomGetName(context , document.data.get("number").toString()) , document.data.get("profileImage").toString()))
                    //Log.d("Status" , "${maps[i].first} =>  ${maps[i].second}")
                }
                presenter.onStatusDataRecived(maps)
            }
    }

}