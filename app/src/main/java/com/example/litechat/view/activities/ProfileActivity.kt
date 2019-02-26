package com.example.litechat.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
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
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity (): AppCompatActivity() {

    var number : String? = null
    final var REQUEST_IMAGE_GET = 1
    var ref : StorageReference? = null
    var mobileNUmber = ""
    var about = " "
    var username = " "
    var image = " "
    var flag = true

  init
    {
        Log.d("Constructor" , "Constructor2")
        mobileNUmber = UserProfileData.UserNumber
        about = UserProfileData.UserAbout
        image = UserProfileData.UserProfileImage
        username = UserProfileData.UserName
    }

    constructor(number: String) : this() {
        mobileNUmber = number
        Log.d("Constructor" , "Constructor")
        flag = false
        getOtherUserData().execute()
    }

    override fun onStart() {
        if(!flag)
        {
            ProfileImageButtonChange.visibility = View.INVISIBLE
            EditAboutButton.visibility = View.GONE
        }
        setContentView(R.layout.activity_profile)
        AboutTextView.text = about
        NameTextView.text = username
        Glide.with(applicationContext).load(image).into(ProfileImageView).onLoadStarted(getDrawable(R.drawable.profile))

        ProfileImageButtonChange.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
        super.onStart()
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var preferances : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferances.getString("CurrentUserNumber" , "123456789")
        ProgressBarProfile.isIndeterminate
        ProgressBarProfile.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        Log.d("MobileNumberPrefer" , preferances.getString("CurrentUserNumber" , "123456789"))
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data!!.data
            Log.d("Image Search" , fullPhotoUri.toString())
            UserProfileData.UserProfileImage = fullPhotoUri.toString()
            ref = FirebaseStorage.getInstance().reference
            ref!!.child(UserProfileData.UserNumber).child("ProfileImage").putFile(fullPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Firebase Storage" , "Image uploaded sucessfully")
                    Glide.with(applicationContext).load(fullPhotoUri).into(ProfileImageView)
                    updateProfileImageOnDatabse()

                }
                .addOnFailureListener {
                    Log.d("Firebase Storage" , "Unable to upload the image")
                    UserProfileData.UserProfileImage  = "https://firebasestorage.googleapis.com/v0/b/litechat-3960c.appspot.com/o/images.png?alt=media&token=d73dedf8-4abb-4bf0-bc65-f980f0bf6f7a"
                    updateProfileImageOnDatabse()
                    Glide.with(applicationContext).load(UserProfileData.UserProfileImage).into(ProfileImageView)
                }
                .addOnCanceledListener {
                    Log.d("Firebase Storage" , "Unable to upload the image")
                    UserProfileData.UserProfileImage  = "https://firebasestorage.googleapis.com/v0/b/litechat-3960c.appspot.com/o/images.png?alt=media&token=d73dedf8-4abb-4bf0-bc65-f980f0bf6f7a"
                    updateProfileImageOnDatabse()
                    Glide.with(applicationContext).load(UserProfileData.UserProfileImage).into(ProfileImageView)
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun updateProfileImageOnDatabse()
    {
        ref!!.child(UserProfileData.UserNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
            UserProfileData.UserProfileImage = it.toString()
            FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).update("profileImage" , UserProfileData.UserProfileImage)
        }
        Log.d("FirebaseStorage" , "${UserProfileData.UserProfileImage}")
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        ProgressBarProfile.visibility = View.INVISIBLE
    }

    inner class getOtherUserData() : AsyncTask<Void , Void , Void>()
    {
        override fun doInBackground(vararg params: Void?): Void? {
            FirebaseFirestore.getInstance().collection("Users").document(mobileNUmber).get().addOnSuccessListener {
                about = it.data!!.get("about").toString()
                username = it.data!!.get("name").toString()
                image = it.data!!.get("profileImage").toString()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            updateView()
            super.onPostExecute(result)
        }

    }

    fun updateView()
    {
        if(!flag)
        {
            ProfileImageButtonChange.visibility = View.INVISIBLE
            EditAboutButton.visibility = View.GONE
        }
        AboutTextView.text = about
        NameTextView.text = username
        Glide.with(applicationContext).load(image).into(ProfileImageView).onLoadStarted(getDrawable(R.drawable.profile))
    }

}

