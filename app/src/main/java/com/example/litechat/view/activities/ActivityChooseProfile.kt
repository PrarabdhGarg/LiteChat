package com.example.litechat.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_choose_profile.*
import java.lang.reflect.Field

class ActivityChooseProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_profile)

       var chooseNumber = editTextChooseNumber.text.toString()

        var db = FirebaseFirestore.getInstance()
        var profileSwitcher=  ProfileSwitcher(chooseNumber)

        buttonChooseNumber.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

               // buttonChooseNumber.isClickable=false
                /*db.collection("Users").document(chooseNumber).set(profileSwitcher).addOnSuccessListener {


                    startActivity(Intent(this@ActivityChooseProfile,HomeActivity::class.java))
                    finish()
                }*/
                /*AllChatDataModel.personalChatList.clear()
                AllChatDataModel.userNumberIdPM=chooseNumber
                finish()*/
            }
        })

    }

    class ProfileSwitcher(number:String){
    var number=number

    }
}
