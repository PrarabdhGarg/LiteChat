package com.example.litechat.contracts

import android.content.Context
import com.example.litechat.view.fragments.FragmentChat
import com.example.litechat.presenter.StatusFragmentPresenter


interface HomeActivityContract {
  
    interface Model{
        fun getUserDataFromFirestore(number : String)
        fun getCurrentActivitiesOfOtherUsers(presenter : StatusFragmentPresenter , context: Context)
        fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter)
    }

    interface Presenter{

        fun getUserDataOnLogin(number : String)
        fun getPersonalChatsFromFirestore()
        fun sortPersonalChatList()
    }

    interface View{

        fun passContext(): Context
        fun isChatFragmentActive(): Boolean
        fun getInstanceOfFragmentChat() : FragmentChat

    }
}