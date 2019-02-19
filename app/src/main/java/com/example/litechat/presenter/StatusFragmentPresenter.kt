package com.example.litechat.presenter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.litechat.contracts.StatusContract
import com.example.litechat.model.UserDataModel
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.io.File

class StatusFragmentPresenter(view : View) : StatusContract.StatusPresenter
{

    var currentView = view
    override fun updateUserInfo(activity : String , image : String) {
        var database = FirebaseFirestore.getInstance()
        UserProfileData.UserCurrentActivity = activity
        //UserProfileData.UserImage = image
        var map : HashMap<String , String> = HashMap()
        if(activity!="")
            map.put("currentActivity" , activity)
        map.put("image" , UserProfileData.UserImage.toString())
        currentView.statusImageView.setImageURI(UserProfileData.UserImage)
        database.collection("Users").document(UserProfileData.UserNumber).set(map , SetOptions.merge())
            .addOnSuccessListener {
            /**
             * Do something if the updating of user is successful
             */
        }
            .addOnFailureListener {
                /**
                 * Do something if data updating fails
                 */
            }
    }

    override fun updateStatusImage(uri: Uri , context: Context) {
        var mStorageRef = FirebaseStorage.getInstance().reference
        var path = uri
        mStorageRef.child(UserProfileData.UserNumber).child("StatusImage").putFile(path).addOnSuccessListener {
            Toast.makeText(context , "Upload Sucessful" , Toast.LENGTH_SHORT).show()
            currentView.statusImageView.setImageURI(path)
        }.addOnFailureListener {
            Toast.makeText(context , "Upload UnSucessful" , Toast.LENGTH_SHORT).show()
        }

    }


}