package com.example.litechat.view.activities

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.litechat.FirebaseService
import com.example.litechat.R
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.*
import com.example.litechat.model.contactsRoom.AppDatabse
import com.example.litechat.presenter.HomeActivityPresenter
import com.example.litechat.view.fragments.FragmentChat
import com.example.litechat.view.fragments.FragmentContact
import com.example.litechat.view.fragments.FragmentStatus
import com.facebook.spectrum.DefaultPlugins
import com.facebook.spectrum.Spectrum
import com.facebook.spectrum.SpectrumSoLoader
import com.facebook.spectrum.logging.SpectrumLogcatLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_group_info.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.io.File
import java.lang.Exception

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
     var asyncTask =  FetchProfileImages()
    private  var doubleBackToExitPressedOnce = false
    private var database =FirebaseFirestore.getInstance()
    inner  class FetchProfileImages: AsyncTask<Void,Void,Void>()
    {
       var currentChatList=ArrayList<ChatObject>()
        override fun doInBackground(vararg params: Void?): Void? {

            Log.d("ImageHomeActivity1"," do Inbackg userNumaber${UserProfileData.UserNumber}")

            database.collection("Users").document(UserProfileData.UserNumber)
                .collection("currentChats").get().addOnSuccessListener { docs ->
                    Log.d("ImageHomeActivity2","inside onSuccess")
                    for(doc in docs)
                    {
                        var chatObject= doc.toObject(ChatObject::class.java)
                        currentChatList.add(chatObject)
                    }
                    Log.d("ImageHomeActivity3","Size of CurrentChatList: ${currentChatList.size}")

                }
            Glide.get(applicationContext).clearDiskCache()

          return null
        }

        override fun onPostExecute(result: Void?) {
            Log.e("ImageHomeActivity", "Enter Post execute")
            for (i in 0 until currentChatList.size )
            {
               try
               {
                   var t = currentChatList[i].otherNumber.toDouble()
                   Log.e("ImageHomeActivity16", "try otherNumber :${currentChatList[i].otherNumber}")
                   saveImageFromFirebaseToDevice(currentChatList[i].otherNumber,
                       "${currentChatList[i].otherNumber}/ProfileImage",currentChatList[i].chatDocumentId)// .jpeg
               }
               catch (e: Exception)
               {
                   Log.e("ImageHomeActivity16", "catch otherNumber :${currentChatList[i].otherNumber}")
                   saveImageFromFirebaseToDevice(currentChatList[i].otherNumber,
                       "groupimages/${currentChatList[i].chatDocumentId}",currentChatList[i].chatDocumentId)// .jpeg
               }
            }

            if(fragmentChat1!=null&&isChatFragmentActive())
            {
                fragmentChat1!!.notifyDataSetChangedToAdapter()
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("FinalCheck", "OnCreateCalled")
        checkForPermission()
        super.onCreate(savedInstanceState)
        /*// initialising Spectrum's SoLoader library
        SpectrumSoLoader.init(this)
        // instantiaiting spectrum object
       var  mSpectrum = Spectrum.make(
             SpectrumLogcatLogger(Log.INFO),
            DefaultPlugins.get()) // JPEG, PNG and WebP plugins
       mSpectrum.transcode(
  EncodedImageSource.from(inputFile),
  EncodedImageSink.from(outputStream),
  TranscodeOptions.Builder(new EncodeRequirement(JPEG, 80)).build(),
  "my_callsite_identifier");*/

        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        homeActivityPresenter = HomeActivityPresenter(this)
        ContentResolverData.contentResolverPassed = contentResolver

        // If user is already logged in, no need to open the LoginActivity again
        if (FirebaseAuth.getInstance().currentUser == null)
        {
            AllChatDataModel.isPresenterCalled = false
            AllChatDataModel.chatScreenStatus = 1
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
            //asyncTask.execute()
            UserProfileData.UserImage = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("StatusImage", Uri.parse(applicationContext.getDrawable(R.drawable.profile).toString()).toString())


            //If the user is already logged in, we need to retreive the users other previously stored data and save it in the local variables

            homeActivityPresenter.getUserDataOnLogin(number)
            if (!AllChatDataModel.isPresenterCalled)
            {
                homeActivityPresenter.getURLFromRoom(applicationContext)
                homeActivityPresenter.getPersonalChatsFromFirestore(applicationContext)
                AllChatDataModel.isPresenterCalled = true
            }

            //This listener is used to get the current id of the device in which the user is signed in
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("Notification", "Failed to retrivve the token correctly")
                } else {
                    val token = it.result?.token
                    Log.d("Notification", "Generated Token = ${token} , ${UserProfileData.UserNumber}")
                    UserProfileData.UserToken = token
                    FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).update("token" , token).addOnSuccessListener {
                        Log.d("Notification", "User Data Token = ${UserProfileData.UserToken}")
                    }

                }
            }
        }
        //TODO We should not start the service when the app is open as there is no point to send push notifications if the app is already running

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
            homeActivityPresenter.getPersonalChatsFromFirestore(applicationContext)
            AllChatDataModel.isPresenterCalled = true
        }
        AllChatDataModel.userNumberIdPM = UserProfileData.UserNumber

        /**
         * This try catch method is used to check if the intent to the [HomeActivity] was generated from a notification,
         * or was it a result of the normal opening of the app
         */

        try {
            Log.d("Notification" , "Calling intent ${intent.extras.getBundle("string")}")
            val intent1 = Intent(this, ChatActivity::class.java)
            val document = intent.getStringExtra("documentPathId")
            Log.d("Notification" , "Document variable $document")
            intent1.putExtra("documentPathId",document)
            intent1.putExtra("string",intent.getStringExtra("string"))
            intent1.putExtra("lastUpdated",intent.getStringExtra("lastUpdated"))
            Log.d("Notification" , "Called intent ${intent1.extras.toString()}")
            if (document != null)
            {
                Log.d("Notification" , "Enterd if of the calling intent")
                AllChatDataModel.chatScreenStatus = 1
                intent = null
                startActivity(intent1)
            }
        }
        catch (e : Exception)
        {
            Log.e("Notifications" , "Entered catch block in the onStart of the Home Activity")
        }

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
            homeActivityPresenter.getURLFromRoom(applicationContext)
            homeActivityPresenter.getPersonalChatsFromFirestore(applicationContext)
            AllChatDataModel.isPresenterCalled = true
        }

        Log.d("Checking", UserProfileData.UserNumber + "   " + AllChatDataModel.userNumberIdPM)
        super.onResume()
    }

    override fun onBackPressed() {
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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `URLCollection` (`chatDocumentId` TEXT NOT NULL, `URL` TEXT NOT NULL, " +
                        "PRIMARY KEY(`chatDocumentId`))")
            }
        }

        val roomdb = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database").addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_1_2).allowMainThreadQueries().build()
        roomdb.urlInfoDao().deleteAllURLData()
        for (item in AllChatDataModel.urlList)
        {
            roomdb.urlInfoDao().insertAllURLdata(item)
        }
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


    private fun checkForPermission()
    {
        if (ContextCompat.checkSelfPermission(this@HomeActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@HomeActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("HomeActivityPermission10","Show expalnation")
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this@HomeActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
                Log.d("HomeActivityPermission10","ask permison")

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("HomeActivityPermission10","finally permison granted")
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    Log.d("HomeActivityPermission10"," permison denioed again")
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun saveImageFromFirebaseToDevice(otherNumber: String, path:String, chatDocumentId: String)
    {
        Log.d("ImageHomeActivity4","Enter save Iamge from firsbase")
        // Check if storage is writable
        Log.d("ImageHomeActivity5",isExternalStorageWritable().toString())

        //checkPermissionGranted()
        if (ContextCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("ImageHomeActivity6","Permission is not granted")
        }
        else
            Log.d("ImageHomeActivity7","Permission is  granted")


        try {
            // Make directory named litechat
            val output =getPrivateAlbumStorageDir(this@HomeActivity,"LiteChat_ProfileImage")
            if (!output!!.exists()) {
                Log.d("ImageHomeActivity8", "Ouput exists" + output.mkdir())
            }


            Log.d("ImageHomeActivity9", "Ouput exits" + output.exists())

            val localFile = File(output, "IMG_${chatDocumentId}_$otherNumber.jpeg")
            if(localFile.exists())
            {
                Log.d("ImageHomeActivity10", "value of delete file :${localFile.delete()} and current state: ${localFile.exists()} newfilecreate: ${localFile.createNewFile()}")
                /*localFile.delete()
                localFile.createNewFile()
*/            }
            Log.d("ImageHomeActivity11","localFile current state :${localFile.exists()} + uri :${localFile.path}")
            val mStorage = FirebaseStorage.getInstance("gs://litechat-3960c.appspot.com")
            val mStorageRef = mStorage.getReference()


            val downloadRef = mStorageRef.getRoot()
                .child(path);// .jpeg
            // Download and get total bytes
            downloadRef.getFile(localFile)
                /*.addOnProgressListener{

                        showProgressNotification(1,title, "",
                            taskSnapshot.getBytesTransferred(),
                            taskSnapshot.getTotalByteCount());

                }*/
                .addOnSuccessListener {

                    Log.d("ImageHomeActivity12", "download:SUCCESS");

                    val uri = Uri.parse(localFile.path)
                    Log.d("HomeActivity13", "download:SUCCESS and URI : $uri path :${localFile.path}")

                }
                .addOnFailureListener {
                    Log.d("ImageHomeActivity14", "onFailure SaveimageFromFirebasetodevice +  " + it.toString())
                }

        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }


    }

    private  fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private  fun getPrivateAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdir()) {
            Log.e("ImageHomeActivity15", "Directory not created previousState :${file.exists()}")
        }
        return file
    }
}

