package com.example.litechat.presenter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.litechat.contracts.LoginContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.UserDataModel
import com.example.litechat.model.UserProfileData
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit


class LoginActivityPresenter (loginView : LoginContract.LoginView): LoginContract.LoginPresenter
{
    var loginActivity = loginView

    /**
     * This function checks if the account of the user already exists in our database or not
     * If the account exists, all the data gets read from firestore and gets stored in the static variables of [UserProfileData]
     * The mobile number also gets stored in the [SharedPreferences], because every time an intent to the gallery or phone is made,
     * the staic variables get destroyed, and we need some identifying parameters to get data again from the database
     * Other information of users is not stored in [SharedPreferences] as the static variables are much easier to use throughout the app
     */

    override fun checkAccountExists (number : String)
    {
        var accountExixts = false
        var preferances : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(loginActivity.getCurrentContext())
        val database = FirebaseFirestore.getInstance()
        database.collection("Users").get().addOnSuccessListener { result ->
            Log.d("iss",result.toString())
            for (document in result) {
                if(document.id == number)
                {
                    accountExixts = true
                    UserProfileData.UserNumber = document.getString("number")
                    Log.d("String" , UserProfileData.UserNumber)
                    UserProfileData.UserName = document.getString("name")
                    UserProfileData.UserCurrentActivity = document.getString("currentActivity")
                    UserProfileData.UserImage = document.getString("image")
                    UserProfileData.UserAbout = document.getString("about")
                    preferances.edit().putString("currentUserNumber" , UserProfileData.UserNumber).apply()
                    Log.d("ProfexistComplete" , UserProfileData.UserName + UserProfileData.UserName + UserProfileData.UserCurrentActivity + UserProfileData.UserImage)
                    loginActivity.onUserAccountFound()
                }
            }
            if(accountExixts == false)
            {
                loginActivity.onUserAccontNotFound()
            }
        }
    }


    /**
     * This function is used to add all the data of a new user to the database
     */

    override fun addUserToFirebase(number : String, id : String, name : String)
    {
        val database = FirebaseFirestore.getInstance()
        val user = UserDataModel(Name = name , Number = number , About = UserProfileData.UserAbout)
        UserProfileData.UserName = name
        UserProfileData.UserNumber = number
        AllChatDataModel.userNumberIdPM =  number
        database.collection("Users").document(number).set(user)
    }

    /**
     * This function verifies that the number provided by the user is in the device or not
     * This is an overloaded fuction, and this one is called if the account of the user already exists
     * It sends an auto-generated verificatioon code to the entered mobile number, and then automatically
     * detects the incoming messages.
     * If the verificaton code matches, the mobile number is verified
     * Still have to add the case where the user wants to use a different mobile number, so that the user can manually enter the verification code
     */

    override fun verifyNumber(number : String, activity : Activity, context: Context)
    {
        var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                    Log.d("Verification", "SMS Verification Sucessful\n$p0")
                    //dialog.visibility = View.INVISIBLE
                    signInWithPhoneAuthCredential(p0!! , context)
                    AllChatDataModel.userNumberIdPM = number
                    Toast.makeText(context, "Verification Sucessfull", Toast.LENGTH_LONG).show()
                }

                override fun onVerificationFailed(p0: FirebaseException?) {
                    Log.d("Verification", "SMS Verification UnSucessful\n$p0")
                    Toast.makeText(context, "Verification UnSucessfull ${p0.toString()}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    super.onCodeSent(p0, p1)
                    Log.d("Verification", "SMS Verification Sucessfully Sent")
                }

            }

        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber("+91$number", 60, TimeUnit.SECONDS, activity, mCallbacks)
    }

    /**
     * This overloaded function is called for a new user
     * It not only verifies the user mobile number, but also adds user information to the database
     */

    override fun verifyNumber(number : String, activity : Activity, context: Context, name: String)
    {
        var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                    Log.d("Verification", "SMS Verification Sucessful\n$p0")
                    //dialog.visibility = View.INVISIBLE
                    addUserToFirebase(number , p0.toString() , name)
                    signInWithPhoneAuthCredential(p0!! , context)
                    Toast.makeText(context, "Verification Sucessfull", Toast.LENGTH_LONG).show()
                }

                override fun onVerificationFailed(p0: FirebaseException?) {
                    Log.d("Verification", "SMS Verification UnSucessful\n$p0")
                    Toast.makeText(context, "Verification UnSucessfull ${p0.toString()}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                    super.onCodeSent(p0, p1)
                    Log.d("Verification", "SMS Verification Sucessfully Sent")
                }

            }

        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber("+91$number", 60, TimeUnit.SECONDS, activity, mCallbacks)
    }

    /**
     * After verification of the mobile number of the user, this function does the task of signing in the user using the [PhoneAuthCredential]
     * provided when the user's mobile number is successfully verified
     */

    override fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, context: Context) {
        var mAuth : FirebaseAuth = FirebaseAuth(FirebaseApp.getInstance())
        mAuth.signInWithCredential(credential).addOnSuccessListener {result ->
            Toast.makeText(context , result.user.toString() , Toast.LENGTH_SHORT).show()
            Log.d("LoginActivityPresenter" , mAuth.currentUser!!.phoneNumber.toString())
            UserProfileData.UserNumber = mAuth.currentUser!!.phoneNumber.toString().substring(3)
            loginActivity.changeActivity()
        }
            .addOnFailureListener { exception ->
                Toast.makeText(context , exception.toString() , Toast.LENGTH_SHORT).show()
                loginActivity.onLoginError()
            }
    }

}