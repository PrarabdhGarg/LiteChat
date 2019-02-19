package com.example.litechat.view.activities

import android.Manifest
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.litechat.R
import com.example.litechat.contracts.LoginContract
import com.example.litechat.presenter.LoginActivityPresenter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_screen.*

class LoginActivity : AppCompatActivity() , LoginContract.LoginView
{
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    var mobileNumber : String? = null
    var firebaseAuth : FirebaseAuth? = null
    var loginActivityPresenter : LoginActivityPresenter? = null   //Stores the instance of the presenter that will be used throughout this activity
    var userName : String? = null
    var doubleBackToExitPressedOnce = false   //This variable is true if the user has pressed the button once. This variable gets reset after every 2 seconds

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)

        FirebaseApp.initializeApp(baseContext)
        loginActivityPresenter = LoginActivityPresenter(this)
        firebaseAuth = FirebaseAuth.getInstance()
        ProgressBar!!.visibility=View.INVISIBLE

        loginButton.setOnClickListener {
            when {
                editTextName.visibility == View.VISIBLE -> {
                    onButtonPressedForSignUp()
                }

                editTextNumber.text.isEmpty() -> editTextNumber.error = "Mobile Number is Compulsory"

                editTextNumber.text.toString().length != 10 -> editTextNumber.error = "Please Enter Valid Number"

                else -> {
                    onFirstButtonPressed()
                }
            }
        }
    }

    /**
     * If the user presses the back button during login once, nothing will happen
     * If he presses it twice within 2 seconds, the app will exit
     * The [finishAffinity] method is used to destroy the present Activity as well as all of its parent activities
     * This was necessary so that the user cannot directly go to the home screen without signing in to his account
     * This feature will only work for android devices with Api greater than 16(android 4.1) because of the [finishAffinity] method
     */

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this , "Press Once More to Exit" , Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onDestroy() {
        loginActivityPresenter = null   //Set the instance of presenter to be null as soon as the activity gets destroyed
        super.onDestroy()
    }

    /**
     * This method is called when the user presses the continue button for the first time to check if the account already
     * exists or not.
     * If the account exists, the mobile number of the user is verified, and then the user is logged into his/her account
     * If the account doesnot exist, then an [editTextName] appears and prompts the user for name
     * After entering the name, the user clicks on the SignUp Button, and the [onButtonPressedForSignUp] method is called
     */

    override fun onFirstButtonPressed()
    {
        Log.d("LOGIN" , "onFirstPressed Callsed")
        mobileNumber = editTextNumber.text.toString()
        ProgressBar!!.setVisibility (View.VISIBLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        loginActivityPresenter!!.checkAccountExists(mobileNumber!!)
    }

    /**
     * This function is called if the user is a new user
     * When the user clicks on the Signup button after entering his/her name, this function is called
     * This function first verifies the mobile number of the user, through the presenter, and if verification is scucessful,
     * a new user is created on the database.
     * The user is also signed into his account after the verification of mobile number is sucessfull
     */

    override fun onButtonPressedForSignUp()
    {
        userName = editTextName.text.toString()
        ProgressBar!!.setVisibility(View.VISIBLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        loginActivityPresenter!!.verifyNumber(mobileNumber!! , this , applicationContext , ProgressBar , userName!!)
    }

    override fun onUserAccontNotFound() {
        /**
         * Ask user to create new Account
         */
        Log.d("TAG " , "New User")
        loginButton.text = "SignUp"
        editTextName.visibility = View.VISIBLE
        ProgressBar!!.visibility = View.INVISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onUserAccountFound() {
        /**
         * Login the User into the app
         */
        Log.d("TAG " , "Existing User")
        ProgressBar!!.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        loginActivityPresenter!!.verifyNumber(mobileNumber!! , this@LoginActivity , applicationContext , dialog = ProgressBar)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun changeActivity() {
        finish()
    }

    override fun onLoginError() {
        Toast.makeText(applicationContext , "Error while logging in the user" , Toast.LENGTH_SHORT).show()
    }

}