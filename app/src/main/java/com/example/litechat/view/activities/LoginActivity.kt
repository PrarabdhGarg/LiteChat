package com.example.litechat.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.example.litechat.R
import com.example.litechat.listeners.CustomListener
import com.example.litechat.listeners.ListenerObject
import com.example.litechat.presenter.LoginActivityPresenter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_screen.*

class LoginActivity : AppCompatActivity()
{
    var mobileNumber : String? = null
    var firebaseAuth : FirebaseAuth? = null
    var loginActivityPresenter : LoginActivityPresenter? = null
    var userName : String? = null
    var doubleBackToExitPressedOnce = false   //This variable is true if the user has pressed the button once. This variable gets reset after every 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        FirebaseApp.initializeApp(baseContext)
        loginActivityPresenter = LoginActivityPresenter()
        firebaseAuth = FirebaseAuth.getInstance()
        ProgressBar!!.visibility=View.INVISIBLE

        var onDataCheckListener : ListenerObject = ListenerObject()

        onDataCheckListener.setCustomObjectListener(object : CustomListener{
            override fun onUserAccountNotFound() {
                /**
                 * Ask user to create new Account
                 */
                Log.d("TAG " , "New User")
                loginButton.text = "SignUp"
                editTextName.visibility = View.VISIBLE
                ProgressBar!!.visibility = View.INVISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            override fun onUserAccountMatch() {
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

        })

        loginButton.setOnClickListener {
            when {
                editTextName.visibility == View.VISIBLE -> {
                    userName = editTextName.text.toString()
                    ProgressBar!!.setVisibility(View.VISIBLE)
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    loginActivityPresenter!!.verifyNumber(mobileNumber!! , this , applicationContext , ProgressBar , userName!!)
                }

                editTextNumber.text.isEmpty() -> editTextNumber.error = "Mobile Number is Compulsory"

                editTextNumber.text.toString().length != 10 -> editTextNumber.error = "Please Enter Valid Number"

                else -> {
                    mobileNumber = editTextNumber.text.toString()
                    ProgressBar!!.setVisibility (View.VISIBLE)
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    loginActivityPresenter!!.checkAccountExists(mobileNumber!! , onDataCheckListener)
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

}
