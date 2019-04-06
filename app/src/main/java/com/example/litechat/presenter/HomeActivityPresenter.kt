package com.example.litechat.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.DataRetrieveClass
import com.example.litechat.model.UserProfileData


class HomeActivityPresenter(val view : HomeActivityContract.View): HomeActivityContract.Presenter{

    private  val dataRetrieveModel = DataRetrieveClass()
    override fun getURLFromRoom(context:Context) {
        dataRetrieveModel.retrieveURLFromRoom(this,context)
    }

    /**
     *This function is called when an already signed in user opens the app again
     * It calls the [getUserDataFromFirestore] method of the model, which populates the static variables with the required information
     */

    override fun getUserDataOnLogin(number: String) {
        dataRetrieveModel.getUserDataFromFirestore(number)
        Toast.makeText(view.passContext() , UserProfileData.UserNumber , Toast.LENGTH_SHORT).show()
    }

    /**
     * Thia function calls the [retrievePersonalChatDataFromFirestore] of the model
     * This method is responsible for providing all the data regarding the currently active chats and also listens for
     * any changes in the chat.
     */

    override fun getPersonalChatsFromFirestore(context: Context) {
        Log.d("FinalDebug2"," homeActivityPresenter.getPersonalChatsFromFirestore called")
        dataRetrieveModel.retrievePersonalChatDataFromFirestore(this,context)
    }

    /**
     * This method is once called at the start of the app when all the currently active chats has been retrived
     * It is also called when a new message arrives, a new chat is created or an existing chat is removed
     * The task of this method is to notify the [AdapterForFragmentChat] that there is a change in the chat list, and
     * then the adapter displays the change appropriately on the screen in the form of a notification icon, new chat,
     * or the removal of a deleted chat
     * TODO Rview the working of this method. Some things are now redundant after the change in logic for notifictions
     */

    override fun sortPersonalChatList() {
        Log.d("FinalDebug6"," sortPersonalChatList() start ")
        /*if(AllChatDataModel.chatScreenStatus==2)
        {
            *//***
             * present in frag and home activity  and not chatting
             *//*
            Log.d("FinalDebug7" , "Error1")

             if(view.isChatFragmentActive())
             {
                 Log.d("FinalDebug7" , "Error2")
                 // Store data
             }
            else
             {
                 if(AllChatDataModel.upadateFragmentChatFirstTime==1)
                 {  // to call only 1 time
                     Log.e("FinalCheck" , "UpdateOneTime")
                     //AllChatDataModel.upadateFragmentChatFirstTime=0
                     Log.d("FinalDebug7"," updateRecyclerViewForFirstTime() Size  ${AllChatDataModel.personalChatList.size}")
                     view.getInstanceOfFragmentChat(). updateRecyclerViewForFirstTime()
                 }
                 else
                 {
                     Log.e("FinalCheck" , "Update other")
                     Log.d("FinalDebug8","all upate${AllChatDataModel.personalChatList.size}")
                     view.getInstanceOfFragmentChat().updateRecyclerViewForFirstTime()
                 }
             }

        }*/

        if (AllChatDataModel.chatScreenStatus == 2 && view.isChatFragmentActive()) {
            Log.d("Try Fragment" , "value ${AllChatDataModel.chatScreenStatus}")
            view.getInstanceOfFragmentChat().updateRecyclerViewForFirstTime()
        }

    }
}