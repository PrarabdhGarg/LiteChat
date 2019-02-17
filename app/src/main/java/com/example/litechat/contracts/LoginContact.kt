package com.example.litechat.contracts

import android.app.Activity
import android.content.Context
import android.widget.ProgressBar
import com.example.litechat.listeners.OnAccountSearchListenerObject
import com.example.litechat.listeners.OnLoginListener
import com.example.litechat.listeners.OnLoginListenerObject
import com.google.firebase.auth.PhoneAuthCredential

interface LoginContact{

    interface Presenter{

        fun checkAccountExists (number : String , listener : OnAccountSearchListenerObject)
        fun addUserToFirebase(number : String , id : String , name : String)
        fun verifyNumber(number : String, activity : Activity, context: Context, dialog: ProgressBar , loginListener : OnLoginListenerObject)
        fun verifyNumber(number : String , activity : Activity , context: Context , dialog: ProgressBar , name: String , loginListener : OnLoginListenerObject)
        fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, context: Context)

    }

    interface View{

        fun onFirstButtonPressed()
        fun onButtonPressedForSignUp()

    }

}