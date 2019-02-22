package com.example.litechat.model

import android.content.Context
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.contactsRoom.User
import com.google.firebase.firestore.FirebaseFirestore

class DataRetriveClass : HomeActivityContract.Model{
    override fun roomGetData(applicationContext: Context): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun roomSetData(applicationContext: Context, userList: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * This class should not be used currently as the structure of firestore is not yet finalized.
     */

    override fun getUserDataFromFirestore(number: String){
        FirebaseFirestore.getInstance().collection("Users").document(number).get()
            .addOnSuccessListener {
                UserProfileData.UserName = it.getString("name")
                UserProfileData.UserNumber = it.getString("number")
            }
    }

}