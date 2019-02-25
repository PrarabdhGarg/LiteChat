package com.example.litechat.model

import android.content.Context

import android.util.Log
import android.widget.Toast
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.HomeActivityPresenter
import com.example.litechat.presenter.StatusFragmentPresenter
import com.google.firebase.firestore.FirebaseFirestore

class DataRetriveClass : HomeActivityContract.Model {

    /**
     * This class should not be used currently as the structure of firestore is not yet finalized.
     */

    override fun getUserDataFromFirestore(number: String){
        FirebaseFirestore.getInstance().collection("Users").document(number).get()
            .addOnSuccessListener {
                UserProfileData.UserName = it.getString("name")
                UserProfileData.UserNumber = it.getString("number")
                UserProfileData.UserCurrentActivity = it.get("currentActivity").toString()
                UserProfileData.UserAbout = it.getString("about").toString()
                UserProfileData.UserImage = it.getString("image").toString()
                UserProfileData.UserProfileImage = it.getString("profileImage").toString()
                Log.d("UserData" , "Data Retrieved class successfully called")
            }
    }


    override fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter) {
        var maps = ArrayList<Pair<String , String>>()
        FirebaseFirestore.getInstance().collection("Users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.id == UserProfileData.UserNumber)
                        continue
                    maps.add(Pair(document.data.getValue("name").toString() , document.data.get("profileImage").toString()))
                    //Log.d("Status" , "${maps[i].first} =>  ${maps[i].second}")
                }
                presenter.onStatusDataRecived(maps)
            }
    }
}