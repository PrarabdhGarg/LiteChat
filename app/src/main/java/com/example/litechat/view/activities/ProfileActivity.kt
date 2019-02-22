package com.example.litechat.view.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.model.UserProfileData
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    var number : String? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        AboutTextView.text = UserProfileData.UserAbout.toString()
        NameTextView.text = UserProfileData.UserName.toString()
        Glide.with(applicationContext).load(UserProfileData.UserProfileImage).into(ProfileImageView)
    }
}
