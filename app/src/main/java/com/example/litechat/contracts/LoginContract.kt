package com.example.litechat.contracts

import android.app.Activity
import android.content.Context
import android.widget.ProgressBar
import com.google.firebase.auth.PhoneAuthCredential

interface LoginContract{

    interface LoginPresenter{

        fun checkAccountExists (number : String)
        fun addUserToFirebase(number : String , id : String , name : String)
        fun verifyNumber(number : String, activity : Activity, context: Context)
        fun verifyNumber(number : String , activity : Activity , context: Context , name: String)
        fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, context: Context)

    }

    interface LoginView{

        fun onFirstButtonPressed()
        fun onButtonPressedForSignUp()
        fun onUserAccontNotFound()
        fun onUserAccountFound()
        fun changeActivity()
        fun onLoginError()
        fun getCurrentContext() : Context

    }

}