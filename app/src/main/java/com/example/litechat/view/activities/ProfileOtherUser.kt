
package com.example.litechat.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile_other_user.*

class ProfileOtherUser : AppCompatActivity() {

    var number : String? = null
    final var REQUEST_IMAGE_GET = 1
    var ref : StorageReference? = null
    var mobile = " "
    var about = " "
    var image = " "
    var names = " "

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_other_user)
        mobile = intent.getStringExtra("number").toString()
        FirebaseFirestore.getInstance().collection("Users").document(mobile).get().addOnSuccessListener {
            names = it.data!!.get("name").toString()
            about = it.data!!.get("about").toString()
            image = it.data!!.get("profileImage").toString()

            AboutTextView.text = about.toString()
            NameTextView.text = names.toString()
            Glide.with(applicationContext).load(image).into(ProfileImageView)

            otherProfileLoader.visibility = View.INVISIBLE
        }



    }

}