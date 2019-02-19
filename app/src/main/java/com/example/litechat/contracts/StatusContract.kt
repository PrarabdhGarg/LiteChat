package com.example.litechat.contracts

import android.content.Context
import android.net.Uri
import com.example.litechat.model.UserDataModel

interface StatusContract{

    interface StatusPresenter{
        fun updateUserInfo(activity: String = "", image: String = "")
        fun updateStatusImage(uri : Uri , context: Context)
    }

}