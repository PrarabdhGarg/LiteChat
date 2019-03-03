package com.example.litechat.presenter

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ContactDataModel
import com.example.litechat.model.ContentResolverData
import com.example.litechat.model.DataRetriveClass
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.User
import com.google.firebase.firestore.FirebaseFirestore


class HomeActivityPresenter(contextPassed: Context , val view : HomeActivityContract.View): HomeActivityContract.Presenter{


    private val context = contextPassed

    private  val dataRetrieveModel = DataRetriveClass()

    override fun getUserDataOnLogin(number: String) {

        dataRetrieveModel.getUserDataFromFirestore(number)
        Toast.makeText(view.passContext() , UserProfileData.UserNumber , Toast.LENGTH_SHORT).show()
    }



    override fun getPersonalChatsFromFirestore() {
        Log.d("FinalDebug2"," homeActivityPresenter.getPersonalChatsFromFirestore called")
        dataRetrieveModel.retrievePersonalChatDataFromFirestore(this)
    }

    override fun sortPersonalChatList() {
        Log.d("FinalDebug6"," sortPersonalChatList() start ")
        if(AllChatDataModel.chatScreenStatus==2)
        {
            /***
             * present in frag and home activity  and not chatting
             */

             if(view.isChatFragmentActive())
             {
                 // Store data
             }
            else
             {
                 if(AllChatDataModel.upadateFragmentChatFirstTime==1)
                 {  // to call only 1 time
                     Log.e("FinalCheck" , "UpdateOneTime")
                     //AllChatDataModel.upadateFragmentChatFirstTime=0
                     Log.d("FinalDebug7"," updateRecyclerViewForFirstTime() Size  ${AllChatDataModel.personalChatList.size}")
                     view.getInstanceOfFragmentChat().updateRecyclerViewForFirstTime()
                 }
                 else
                 {
                     Log.e("FinalCheck" , "Update other")
                     Log.d("FinalDebug8","all upate${AllChatDataModel.personalChatList.size}")
                     view.getInstanceOfFragmentChat().updateRecyclerView()
                 }
             }

        }

    }
}