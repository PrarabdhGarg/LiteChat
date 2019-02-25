package com.example.litechat.contracts

import android.content.Context
import android.net.Uri
import com.example.litechat.model.UserDataModel

interface StatusContract{

    interface StatusPresenter{
        fun updateUserInfo(activity: String = "")
        fun updateStatusImage(uri : Uri)
        fun getInfoForRecyclerView()
        fun onStatusDataRecived(map : ArrayList<Pair<String , String>>)
    }

    interface View{
        fun onNewStatusImageSelected(reference : Uri)
        fun onNewDataRecivedForRecyclerView(maps1 : ArrayList<Pair<String, String>>)
        fun getCurrentContext() : Context
        fun setStatusImageView(path : String)
    }

}