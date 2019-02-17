package com.example.litechat.view.fragments

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
import com.example.litechat.view.activities.HomeActivity


class FragmentStatus: Fragment() {

    var statusEditButton : ImageButton? = null
    var currentActivityEditText : EditText? = null
    var statusEditDoneButton : ImageButton? = null
    val REQUEST_IMAGE_GET = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.example.litechat.R.layout.fragment_status, container, false)
        statusEditButton = view.findViewById(com.example.litechat.R.id.EditStatusImageView)
        currentActivityEditText = view.findViewById(com.example.litechat.R.id.currentActivityTextView)
        statusEditDoneButton = view.findViewById(com.example.litechat.R.id.EditStatusDoneImageView)
        return view
    }

    override fun onStart() {
        super.onStart()
        //Make and Call a function to get and display data

        statusEditButton!!.setOnClickListener {
            Toast.makeText(context , "Clicked" , Toast.LENGTH_SHORT).show()
            statusEditDoneButton!!.visibility = View.VISIBLE
            statusEditButton!!.visibility = View.INVISIBLE
            currentActivityEditText!!.inputType = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
        }

        statusEditDoneButton!!.setOnClickListener {
            statusEditDoneButton!!.visibility = View.INVISIBLE
            statusEditButton!!.visibility = View.VISIBLE
            currentActivityEditText!!.inputType = InputType.TYPE_NULL
        }

        changeStatusImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            activity!!.startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

    }

}