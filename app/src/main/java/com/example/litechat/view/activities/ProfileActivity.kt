
package com.example.litechat.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    var number : String? = null
    final var REQUEST_IMAGE_GET = 1
    var ref : StorageReference? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        AboutTextView.setText(UserProfileData.UserAbout.toString())
        NameTextView.text = UserProfileData.UserName.toString()
        Glide.with(applicationContext).load(UserProfileData.UserProfileImage).into(ProfileImageView).onLoadStarted(getDrawable(R.drawable.profile))

        ProfileImageButtonChange.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        LinearLayoutButton.setOnClickListener(View.OnClickListener {
            if (EditAboutButton.text=="Edit Information")
            {
                EditAboutButton.text= "Finish Editing"
                Glide.with(applicationContext).load(R.drawable.ic_check_black_24dp).into(editImage)
                AboutTextView.inputType=InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
                AboutTextView.setHorizontallyScrolling(false)
            }
            else
            {
                EditAboutButton.text="Edit Information"
                Glide.with(applicationContext).load(R.drawable.ic_check_black_24dp).into(editImage)
                AboutTextView.inputType=InputType.TYPE_NULL
                AboutTextView.setLines(5)
                AboutTextView.setHorizontallyScrolling(false)
                updateAboutInfo()

            }

        })

    }

    private fun updateAboutInfo() {

        ProgressBarProfile.isIndeterminate
        ProgressBarProfile.visibility=View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        UserProfileData.UserAbout=AboutTextView.text.toString()
        FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber)
            .update("about",UserProfileData.UserAbout).addOnSuccessListener {

                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                ProgressBarProfile.visibility=View.INVISIBLE
                Toast.makeText(applicationContext, "Updated your information successfully",Toast.LENGTH_SHORT).show()
            }

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

            var drawablee = resources.getDrawable(R.drawable.prarabdh)
             var  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
          //  val stream = ByteArrayOutputStream()

         //   val stream = FileInputStream(File("path/to/images/rivers.jpg"))
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,40,baos)
         //   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data1 = baos.toByteArray()

            var uploadTask =  ref!!.child(UserProfileData.UserNumber).child("ProfileImage").putBytes(data1)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Log.d("Firebase Storage" , "Image uploaded sucessfully")
                Glide.with(applicationContext).load(fullPhotoUri).into(ProfileImageView).onLoadStarted(getDrawable(R.drawable.profile))
                updateProfileImageOnDatabse()
            }

           /* ref!!.child(UserProfileData.UserNumber).child("ProfileImage").putFile(fullPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Firebase Storage" , "Image uploaded sucessfully")
                    Glide.with(applicationContext).load(fullPhotoUri).into(ProfileImageView).onLoadStarted(getDrawable(R.drawable.profile))
                    updateProfileImageOnDatabse()
                }*/
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun updateProfileImageOnDatabse()
    {
        ref!!.child(UserProfileData.UserNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
            UserProfileData.UserProfileImage = it.toString()
        }
        Log.d("FirebaseStorage" , "${UserProfileData.UserProfileImage}")
        FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).update("profileImage" , UserProfileData.UserProfileImage)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        ProgressBarProfile.visibility = View.INVISIBLE
    }

}
