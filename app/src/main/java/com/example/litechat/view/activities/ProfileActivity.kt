package com.example.litechat.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.lang.Exception
import java.nio.file.Files

/**
 * This class doesn't follow the MVP structure
 * This is because by the time this class was written, all of us were so tired and frustrated that we thought to just write
 * a simple class, and make the code work as soon as possible
 */

class ProfileActivity : AppCompatActivity() {

    var number : String? = null
    val REQUEST_IMAGE_GET = 1   //Request code for selecting an image from the gallery
    var ref : StorageReference? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ProfileImage" , "profile img = ${UserProfileData.UserProfileImage}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        AboutTextView.setText(UserProfileData.UserAbout.toString())
        NameTextView.text = UserProfileData.UserName.toString()
        //Load default photo if profile image of user does not exist
        /**
         * Check why this shows an error
         */
        Glide.with(applicationContext).load(UserProfileData.UserProfileImage).apply(RequestOptions().placeholder(applicationContext.getDrawable(R.drawable.profile)))
            .into(ProfileImageView)

        //OnClick listener for the gallery button to open the gallery
        ProfileImageButtonChange.setOnClickListener {
            Log.d("Debug111" , "GalleryOpened")
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        //OnClick listener for the text view and image button of edit about info together
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

    /**
     * This function is called when the user clicks on the button to finish editing his about information
     * The function first makes the window untouchable and displays the progress bar
     * It then updates the data on the firestore account, and once the account is sucessfully updated, the local static variables
     * are also updated, so that the changes are reflected on the current app as well
     * TODO : When the user clicks on the edit text to edit the information, all the text gets wraped into a single line, making it difficult for the user to update.
     */

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

    /**
     * This function is called when the user selects an image from the gallery
     * The function first retrieves the phone number stored in the shared preferences because the static variables are destroyed
     * when the gallery is opened.
     * THen if there is no problem with the selected image, the image is first uploaded on the firebase storage, and then the function
     * [updateProfileImageOnDatabse] is called
     */

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("MobileProfile" , "Number = ${UserProfileData.UserNumber}")
        /*var preferances : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferances.getString("CurrentUserNumber" , "123456789")*/

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data!!.data.toString().contains("image")) {
            //val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            ProgressBarProfile.isIndeterminate
            ProgressBarProfile.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            val fullPhotoUri: Uri? = data!!.data
            Log.d("Image Search" , fullPhotoUri.toString())
            UserProfileData.UserProfileImage = fullPhotoUri.toString()
            ref = FirebaseStorage.getInstance().reference
            var byteArray : ByteArray? = null
            /**try {
                var temp
                byteArray = Files.readAllBytes(Compressor(this).compressToFile(File(fullPhotoUri!!.path)).toPath())
            }catch( e : Exception){
                e.printStackTrace()
            }*/

            ref!!.child(UserProfileData.UserNumber).child("ProfileImage").putFile(fullPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Firebase Storage" , "Image uploaded sucessfully")
                    ProfileImageView.setImageURI(fullPhotoUri)
                    UserProfileData.UserProfileImage = fullPhotoUri!!.path
                    updateProfileImageOnDatabse()
                }
        }
        else
        {
            Toast.makeText(applicationContext , "Please select a valid image" , Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * This function is called after the user has sucessfully selected a profile image
     * The function retrives the URL of the image from firebase storage, and once that is sucessfull, it updates the url of the new image on the firebsase database,
     * as well as in the local variable [UserProfileData.UserProfileImage] which updates the image on the screen as well
     * The function then makes the screen touchable again and hides the progress bar
     */

    fun updateProfileImageOnDatabse()
    {
        ref!!.child(UserProfileData.UserNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
            var downloadUrl = it.toString()
            FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).update("profileImage" , downloadUrl).addOnCompleteListener {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                ProgressBarProfile.visibility = View.INVISIBLE
                UserProfileData.UserProfileImage = downloadUrl
            }
        }
        Log.d("FirebaseStorage" , "${UserProfileData.UserProfileImage}")

    }

}
