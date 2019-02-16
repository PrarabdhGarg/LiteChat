package com.example.litechat.presenter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.litechat.listeners.ListenerObject
import com.example.litechat.model.UserDataModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.util.concurrent.TimeUnit


class LoginActivityPresenter
{
    fun checkAccountExists (number : String , listener : ListenerObject)
    {
        var flag = false
        val database = FirebaseFirestore.getInstance()
        database.collection("Users").get().addOnSuccessListener { result ->
            for (document in result) {
                if(document.id == number)
                {
                    flag = true
                    listener.listener!!.onUserAccountMatch()
                }
            }
            if(flag == false)
            {
                listener.listener!!.onUserAccountNotFound()
            }
        }
    }

    fun addUserToFirebase(number : String , id : String , name : String)
    {
        val database = FirebaseFirestore.getInstance()
        val user : UserDataModel = UserDataModel(id = id , name = name , number = number)
        database.collection("Users").add(user)
    }

    fun verifyNumber(number : String , activity : Activity , context: Context , dialog: ProgressBar)
    {

        var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                    Log.d("Verification", "SMS Verification Sucessful\n$p0")
                    dialog.visibility = View.INVISIBLE
                    Toast.makeText(context, "Verification Sucessfull", Toast.LENGTH_LONG).show()
                }

                override fun onVerificationFailed(p0: FirebaseException?) {
                    Log.d("Verification", "SMS Verification UnSucessful\n$p0")
                    Toast.makeText(context, "Verification UnSucessfull", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    super.onCodeSent(p0, p1)
                    Log.d("Verification", "SMS Verification Sucessfully Sent")
                }

            }

        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber("+91$number", 60, TimeUnit.SECONDS, activity, mCallbacks)
    }

    fun verifyNumber(number : String , activity : Activity , context: Context , dialog: ProgressBar , name: String)
    {

        var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                    Log.d("Verification", "SMS Verification Sucessful\n$p0")
                    dialog.visibility = View.INVISIBLE
                    addUserToFirebase(number , p0.toString() , name)
                    Toast.makeText(context, "Verification Sucessfull", Toast.LENGTH_LONG).show()
                }

                override fun onVerificationFailed(p0: FirebaseException?) {
                    Log.d("Verification", "SMS Verification UnSucessful\n$p0")
                    Toast.makeText(context, "Verification UnSucessfull", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    super.onCodeSent(p0, p1)
                    Log.d("Verification", "SMS Verification Sucessfully Sent")
                }

            }

        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber("+91$number", 60, TimeUnit.SECONDS, activity, mCallbacks)
    }




}