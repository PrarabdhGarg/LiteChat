package com.example.litechat.view.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_status.*
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.net.Uri
import android.text.style.LineHeightSpan
import com.example.litechat.model.UserDataModel
import com.example.litechat.model.UserProfileData
import com.example.litechat.presenter.StatusFragmentPresenter
import com.example.litechat.view.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_status.view.*


class FragmentStatus: Fragment(){

    var stausFragmentPresenter : StatusFragmentPresenter? = null
    val REQUEST_IMAGE_GET = 1   //Request code for getting image from the gallery is 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.example.litechat.R.layout.fragment_status, container, false)
        stausFragmentPresenter = StatusFragmentPresenter(view!!)
        return view
    }

    override fun onStart() {
        super.onStart()
        //Make and Call a function to get and display data
        view!!.statusImageView.setImageURI(UserProfileData.UserImage)    //On starting the fragment, load the current image in the image view, whose Uri is stored locally
        view!!.EditStatusImageView!!.setOnClickListener {
            Toast.makeText(context , "Clicked" , Toast.LENGTH_SHORT).show()
            view!!.EditStatusDoneImageView!!.visibility = View.VISIBLE
            view!!.EditStatusImageView!!.visibility = View.INVISIBLE
            view!!.currentActivityTextView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }

        view!!.EditStatusDoneImageView!!.setOnClickListener {
            view!!.EditStatusDoneImageView!!.visibility = View.INVISIBLE
            view!!.EditStatusImageView!!.visibility = View.VISIBLE
            view!!.currentActivityTextView.inputType = InputType.TYPE_NULL
            //Think of some method to prevent user input while data is getting updated on fireStore
            stausFragmentPresenter!!.updateUserInfo(activity = view!!.currentActivityTextView.text.toString())
        }

        changeStatusImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            activity!!.startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    /**
     * This function is called from the parent Activity [HomeActivity] after it sucessfully gets the path(Uri) of the selected image
     * It was necessary to call this function from the [HomeActivity] as the result of the intent to open the gallery can only be received
     * in the parent Activity
     * This function then calls a function in the [StatusFragmentPresenter] which changes the image displayed on the screen as well as
     * adds the image to the Firebase Storage so that other people can also view the image
     */

    fun onNewStatusImageSelected(reference : Uri)
    {
        Toast.makeText(context , "Please wait while we update the photo" , Toast.LENGTH_SHORT).show()
        stausFragmentPresenter!!.updateStatusImage(reference)
    }
}