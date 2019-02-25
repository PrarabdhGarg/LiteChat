package com.example.litechat.presenter

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.ContactDataModel
import com.example.litechat.model.ContentResolverData
import com.example.litechat.model.DataRetriveClass
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.User
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivityPresenter(contextPassed: Context): HomeActivityContract.Presenter{

    private val context = contextPassed

    override fun getUserDataOnLogin(number: String) {

        DataRetriveClass().getUserDataFromFirestore(number)
        Toast.makeText( context, UserProfileData.UserNumber , Toast.LENGTH_SHORT).show()
    }
}