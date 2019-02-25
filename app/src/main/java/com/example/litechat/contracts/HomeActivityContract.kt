package com.example.litechat.contracts

import android.content.ContentResolver
import android.content.Context
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.HomeActivityPresenter
import com.example.litechat.presenter.StatusFragmentPresenter


interface HomeActivityContract {

    interface Presenter{

        fun getUserDataOnLogin(number : String)

    }

    interface Model{

        fun getUserDataFromFirestore(number : String)
        fun getCurrentActivitiesOfOtherUsers(presenter : StatusFragmentPresenter)

    }

}