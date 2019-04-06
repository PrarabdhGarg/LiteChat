package com.example.litechat.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_group_info.*
import kotlinx.android.synthetic.main.activity_profile_other_user.*
import java.io.File
import java.lang.Exception

/**
 * This activity is called when the user clicks on the profile image of another user
 * It is very similar to the [ProfileActivity] with the only changes being that the buttons to edit profile image
 * and about information are missing
 */

class ProfileOtherUser : AppCompatActivity() {

    var number=" "
    var id=" "
    var about = " "
    var image = " "
    var names = " "
    var url =" "

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = intent.getStringExtra("documentPathId")
        number = intent.getStringExtra("number").toString()
        // show previous profile image
        url = intent.getStringExtra("url")


        setContentView(R.layout.activity_profile_other_user)

        Log.d("URLL", url.toString())
        if(url!=" ")
            Glide.with(applicationContext).load(url).apply(RequestOptions().placeholder(applicationContext.getDrawable(R.drawable.profile))).into(ProfileImageView)
        else
            Glide.with(applicationContext).load(com.example.litechat.R.drawable.profile).into(ProfileImageView)

        Log.d("ImageProfileOtherImageUpadte", "image dowwnloaded to be updated in device otherNumber :$number")

        FirebaseFirestore.getInstance().collection("Users").document(number).get().addOnSuccessListener {
            names = it.data!!.get("name").toString()
            about = it.data!!.get("about").toString()
            image = it.data!!.get("profileImage").toString()

            AboutTextView.text = about.toString()
            NameTextView.text = names.toString()
            otherProfileLoader.visibility=View.GONE
            try
            {
                Glide.with(applicationContext).load(image).apply(RequestOptions().placeholder(applicationContext.getDrawable(R.drawable.profile))).into(ProfileImageView)
                AllChatDataModel.urlList.find { it.chatDocumentId==id }!!.URL=image
            }
            catch (e: Exception)
            {

            }


        }

    }


    override fun onBackPressed() {
        AllChatDataModel.chatScreenStatus = 2
        super.onBackPressed()
    }

}
