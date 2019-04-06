package com.example.litechat.view.activities

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.example.litechat.FirebaseService
import com.example.litechat.NotificationService
import com.example.litechat.R
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.*
import com.example.litechat.presenter.HomeActivityPresenter
import com.example.litechat.view.fragments.FragmentChat
import com.example.litechat.view.fragments.FragmentContact
import com.example.litechat.view.fragments.FragmentStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_status.view.*

class HomeActivity : AppCompatActivity(), HomeActivityContract.View,SearchView.OnQueryTextListener {


    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var fragment: FragmentStatus? = null
    private var fragmentChat1: FragmentChat? = null
    var fragmentContact: FragmentContact? = null
    lateinit var homeActivityPresenter: HomeActivityPresenter
    var serviceIntent: Intent? = null
    private  var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("FinalCheck", "OnCreateCalled")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        homeActivityPresenter = HomeActivityPresenter(this)
        ContentResolverData.contentResolverPassed = contentResolver

        // If user is already logged in, no need to open the LoginActivity again
        if (FirebaseAuth.getInstance().currentUser == null)
        {
            AllChatDataModel.isPresenterCalled = false
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        }
        else
        {
            //If user is already logged in, get its number from shared preferences, store it in the static variable and call the homeActivity presenter to retrieve currently active chats
            var number = PreferenceManager
                .getDefaultSharedPreferences(applicationContext)
                .getString("currentUserNumber", "123456789")
            Log.d("HomeActivity", "Else entered in auth.getInstance $number")
            //Start the service for firebase cloud messaging
            startService(Intent(this , FirebaseService::class.java))

            //Update the important static variables of the class from the data stored in  Shared Preferences
            UserProfileData.UserNumber = number
            AllChatDataModel.userNumberIdPM = number
            UserProfileData.UserImage = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("StatusImage", Uri.parse(applicationContext.getDrawable(R.drawable.profile).toString()).toString())

            //If the user is already logged in, we need to retreive the users other previously stored data and save it in the local variables
            homeActivityPresenter.getUserDataOnLogin(number)
            if (!AllChatDataModel.isPresenterCalled)
            {
                homeActivityPresenter.getPersonalChatsFromFirestore()
                AllChatDataModel.isPresenterCalled = true
            }

            //This listener is used to get the current id of the device in which the user is signed in
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("Notification", "Failed to retrivve the token correctly")
                } else {
                    val token = it.result?.token
                    Log.d("Notification", "Generated Token = ${token}")
                    UserProfileData.UserToken = token
                    FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).update("token" , token)
                    Log.d("Notification", "User Data Token = ${UserProfileData.UserToken}")
                }
            }
        }
        //TODO We should not start the service when the app is open as there is no point to send push notifications if the app is already running
        //TODo We wont be needing a seperate notification service if FCM is working
        /*serviceIntent = Intent(this, NotificationService::class.java)
        var servicceStatus: String? = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("service", "no")
        startService(serviceIntent)
        if (servicceStatus == "no") {
            startService(serviceIntent)
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString("service", "yes").apply()
        }*/

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Log.d("MobileOnCreate", UserProfileData.UserNumber)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
      val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            var component=ComponentName(this@HomeActivity,SearchResultsActivity::class.java)
            setSearchableInfo(searchManager.getSearchableInfo(component))
            this.isSubmitButtonEnabled=true
            setIconifiedByDefault(true)
        }
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
         return false
    }


    override fun onStart() {
        super.onStart()
        Log.e("FinalCheck", "OnStartCalled")
        //Clear all the data stored previously in the static data
        // AllChatDataModel.personalChatList.clear()
        if (!AllChatDataModel.isPresenterCalled)
        {
            homeActivityPresenter.getPersonalChatsFromFirestore()
            AllChatDataModel.isPresenterCalled = true
        }
        AllChatDataModel.userNumberIdPM = UserProfileData.UserNumber
        Log.d("FinalDebug1", " homeActivityPresenter.getPersonalChatsFromFirestore called with ${AllChatDataModel.userNumberIdPM}")
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            var fragmentChat = FragmentChat()
            when (position) {

                0 -> {
                    Log.d("Position" , "Position1 called")
                    fragmentChat = FragmentChat()
                    fragmentChat1 = fragmentChat
                    return fragmentChat
                }

                1 -> {
                    fragmentContact = FragmentContact()
                    return fragmentContact!!
                }

                2 -> {
                    val fragmentStatus = FragmentStatus()
                    fragment = fragmentStatus
                    return fragmentStatus
                }

            }

            return fragmentChat
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * This function is called when the user selects an image from the gallery
     * The function first retrieves the phone number stored in the shared preferences because the static variables are destroyed
     * when the gallery is opened. It stores the number in the static variable once again so that it can be reused
     * Then if there is no problem with the selected image, the function passes the path(URI) of the selected image to Fragment Status
     * where further actions are taken on the image
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MobileNumber" , "Number = ${UserProfileData.UserNumber}")
        var preferances: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferances.getString("CurrentUserNumber", "123456789")
        Log.d("MobileNumberPrefer", preferances.getString("CurrentUserNumber", "123456789"))
        fragment!!.view!!.statusLoader.visibility = View.VISIBLE
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data!!.data.toString().contains("image")) {
            //val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data!!.data
            Log.d("Image Search", fullPhotoUri.toString())
            /**Shift this line to onSucess Listener for uploading of the photo*/
            UserProfileData.UserImage = fullPhotoUri.toString()
            fragment!!.onNewStatusImageSelected(fullPhotoUri!!)
        }
        else
        {
            Toast.makeText(applicationContext , "Please select a valid image" , Toast.LENGTH_LONG).show()
        }
    }

    override fun passContext(): Context =
        applicationContext   //function to pass contentResolver to HomeActivityPresenter

    override fun isChatFragmentActive(): Boolean
    {
        if(container.currentItem == 0)
        {
            Log.d("Container" , "Returned True")
            Log.d("Container" , "Returned fragment $fragmentChat1")
            return true
        }
        return false //function to return whether chat Fragment is displayed or not
    }


    override fun getInstanceOfFragmentChat(): FragmentChat =
        fragmentChat1!!  //Function to return the current instance of fragment chat

    override fun onResume() {
        //AllChatDataModel.upadateFragmentChatFirstTime=1
        Log.e("FinalCheck", "onResumeCalled")
        Log.d("Debug", "On Resume of main activity called with user ${UserProfileData.UserNumber}")

        if (!AllChatDataModel.isPresenterCalled)
        {
            homeActivityPresenter.getPersonalChatsFromFirestore()
            AllChatDataModel.isPresenterCalled = true
        }

        Log.d("Checking", UserProfileData.UserNumber + "   " + AllChatDataModel.userNumberIdPM)
        super.onResume()
    }

    override fun onBackPressed() {
        stopService(serviceIntent)
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        else
          container.currentItem = 0

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this , "Press Once More to Exit" , Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1000)

    }

    override fun onDestroy() {
        AllChatDataModel.isPresenterCalled = false
        Log.d("Notification", "onDestroy of HomeActivity called")
        stopService(serviceIntent)
        super.onDestroy()
    }

    override fun onStop() {
        AllChatDataModel.isPresenterCalled = false
        Log.d("Notifications", "onStop of HomeActivity called")
        super.onStop()
    }

    override fun onDetachedFromWindow() {
        Log.d("Notification", "onDetachedFromWindowCalled")
        super.onDetachedFromWindow()
    }


}

