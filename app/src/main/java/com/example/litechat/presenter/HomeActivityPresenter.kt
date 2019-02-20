package com.example.litechat.presenter

import android.content.Context
import android.view.ContextMenu
import android.view.View
import android.widget.Toast
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.DataRetriveClass
import com.example.litechat.model.UserProfileData

class HomeActivityPresenter(context : Context) : HomeActivityContract.HomeActivityPresenter{
    var contextCurrent = context

    override fun getUserDataOnLogin(number: String) {
        DataRetriveClass().getUserDataFromFirestore(number)
        Toast.makeText(contextCurrent , UserProfileData.UserNumber , Toast.LENGTH_SHORT).show()
    }

}