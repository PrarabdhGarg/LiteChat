package com.example.litechat.contracts

import com.example.litechat.model.UserDataModel

interface HomeActivityContract{

    interface HomeActivityPresenter{
        fun getUserDataOnLogin(number : String)
    }

    interface HOmeActivityModel{
        fun getUserDataFromFirestore(number : String)
    }
}