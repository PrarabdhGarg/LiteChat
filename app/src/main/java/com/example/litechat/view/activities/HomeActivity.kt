package com.example.litechat.view.activities


import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import com.example.litechat.R
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.listeners.BoomListener
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ContactsModel
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.*
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.HomeActivityPresenter
import com.example.litechat.view.fragments.FragmentChat
import com.example.litechat.view.fragments.FragmentContact
import com.example.litechat.view.fragments.FragmentStatus
import com.google.firebase.auth.FirebaseAuth
import com.nightonke.boommenu.BoomButtons.HamButton
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.util.ArrayList


class HomeActivity : AppCompatActivity(),HomeActivityContract.View,SearchView.OnQueryTextListener
{


    override fun passContext(): Context = applicationContext

    override fun isChatFragmentActive(): Boolean {
        return  chatFragmentActive
    }

    override fun getInstanceOfFragmentChat(): FragmentChat {
        return fragmentChat1!!
    }


    //function to pass contentResolver to HomeActivityPresenter

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var fragment : FragmentStatus? = null
    private var fragmentChat1 : FragmentChat? = null
    private var chatFragmentActive= false

    override fun getPersonalChats() {
       // to be removed
    }

    lateinit var homeActivityPresenter: HomeActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("FinalCheck" , "OnCreateCalled")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeActivityPresenter = HomeActivityPresenter(applicationContext , this)

        ContentResolverData.contentResolverPassed = contentResolver
       // If user is already logged in, no need to open the LoginActivity again


        if(FirebaseAuth.getInstance().currentUser == null)
        {
            startActivity(Intent(this@HomeActivity , LoginActivity::class.java))
        }
        else{

            var number = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("currentUserNumber" , "123456789")
            Log.d("HomeActivity" , "Else enterd in auth.getIstance $number")
            //AllChatDataModel.upadateFragmentChatFirstTime = 1
            UserProfileData.UserNumber = number
            AllChatDataModel.userNumberIdPM = number
            homeActivityPresenter.getUserDataOnLogin(number)

                homeActivityPresenter.getPersonalChatsFromFirestore()
                AllChatDataModel.isPresenterCalled = true

        }


        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Log.d("MobileOnCreate" , UserProfileData.UserNumber)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))

        }
        return true
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(text: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
    super.onStart()
        Log.e("FinalCheck" , "OnStartCalled")
        AllChatDataModel.personalChatList.clear()

            homeActivityPresenter.getPersonalChatsFromFirestore()
            AllChatDataModel.isPresenterCalled = true

        AllChatDataModel.userNumberIdPM = UserProfileData.UserNumber
       Log.d("FinalDebug1"," homeActivityPresenter.getPersonalChatsFromFirestore called with ${AllChatDataModel.userNumberIdPM}")
    }


  //  override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    //    val id = item.itemId


  /*      when (id) {
            R.id.action_profile -> {

                // start Activity for Profile
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                return true
            }
            R.id.action_developers -> {

                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(this@HomeActivity, DeveloperActivity::class.java))
                return true
            }
            R.id.action_signOut -> {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                FirebaseAuth.getInstance().signOut()
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString("CurrentUserNumber" , "").apply()
                UserProfileData.clearData()
                startActivity(Intent(this@HomeActivity , LoginActivity::class.java))
                return true
            }

            R.id.action_newPersonalChat -> {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                Log.d("AllChatNumber" , AllChatDataModel.userNumberIdPM)
                startActivity(Intent(this@HomeActivity,NewPersonalChatActivity::class.java))
                return true
            }


            R.id.action_newGroupChat -> {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                Log.d("AllChatNumber" , AllChatDataModel.userNumberIdPM)
                startActivity(Intent(this@HomeActivity,NewGroupChatActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }*/
      //  }



    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            val fragmentChat = FragmentChat()
            when(position){
                0  -> {
                    chatFragmentActive=true
                    fragmentChat1 = fragmentChat
                    return fragmentChat }

                1   -> { val fragmentContact= FragmentContact()
                    chatFragmentActive=false
                    return fragmentContact}


                2   ->{ val fragmentStatus= FragmentStatus()
                    chatFragmentActive=false
                    fragment = fragmentStatus
                    return fragmentStatus }

            }

            return   fragmentChat
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        var preferances : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferances.getString("CurrentUserNumber" , "123456789")
        Log.d("MobileNumberPrefer" , preferances.getString("CurrentUserNumber" , "123456789"))
        fragment!!.view!!.statusLoader.visibility = View.VISIBLE
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data!!.data
            Log.d("Image Search" , fullPhotoUri.toString())
            UserProfileData.UserImage = fullPhotoUri.toString()
            fragment!!.onNewStatusImageSelected(fullPhotoUri!!)
        }
    }

    override fun onResume() {
        //AllChatDataModel.upadateFragmentChatFirstTime=1
        Log.e("FinalCheck" , "onResumeCalled")
        Log.d("Debug" , "On Resume of main activity called with user ${UserProfileData.UserNumber}")

            homeActivityPresenter.getPersonalChatsFromFirestore()

        Log.d("Checking" , UserProfileData.UserNumber+"   " + AllChatDataModel.userNumberIdPM)
        super.onResume()
    }

}

