package com.example.litechat.model

import com.example.litechat.contracts.HomeActivityContract
import com.google.firebase.firestore.FirebaseFirestore

class DataRetriveClass : HomeActivityContract.HOmeActivityModel {

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