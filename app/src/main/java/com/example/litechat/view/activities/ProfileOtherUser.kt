package com.example.litechat.view.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_other_user.*

/**
 * This activity is called when the user clicks on the profile image of another user
 * It is very similar to the [ProfileActivity] with the only changes being that the buttons to edit profile image
 * and about information are missing
 */

class ProfileOtherUser : AppCompatActivity() {

    var number : String? = null
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
            Glide.with(applicationContext).load(image).apply(RequestOptions().placeholder(applicationContext.getDrawable(R.drawable.profile))).into(ProfileImageView)

            otherProfileLoader.visibility = View.INVISIBLE
        }

    }

    override fun onBackPressed() {
        AllChatDataModel.chatScreenStatus = 2
        super.onBackPressed()
    }

}