package com.example.litechat.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.util.Log
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.contactsRoom.AppDatabse
import com.example.litechat.model.contactsRoom.URLInfo
import com.example.litechat.presenter.StatusFragmentPresenter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

/**
 * This class handles the major data retrieval from the database
 * It contains the snapshotListener which monitors any changes to the structure and notifies recycler view of the changes
 * in the chats appropriately
 */

class DataRetrieveClass : HomeActivityContract.Model{

    private val db = FirebaseFirestore.getInstance()
    private  var roomdb : AppDatabse? =null

    /**
     * This function is called whenever a signed in user opens the app once again
     * It retrieves existing data of the user from the database and stores all of it in the static variables for use
     * throughout the app
     */

    override fun getUserDataFromFirestore(number: String){
        db.collection("Users").document(number).get()
            .addOnSuccessListener {
                UserProfileData.UserName = it.getString("name")
                UserProfileData.UserNumber = it.getString("number")
                UserProfileData.UserCurrentActivity = it.get("currentActivity").toString()
                UserProfileData.UserAbout = it.getString("about").toString()
                UserProfileData.UserProfileImage = it.getString("profileImage").toString()
                Log.d("UserData" , "Data Retrieved class successfully called")
            }
    }

    /**
     * This function contains the listener that listens for any changes in the chats structure.
     * It is once called when the listener attaches itself, and this helps in displaying the list of currently active chats
     * Whenever a new chat comes, the chat object is added to the [AllChatDataModel.personalChatList] and then the
     * presenter.sortPersonalChatList() method is called which informs the recycler view about the update
     * The recycler view adapter handles if the chat is new, displays it, otherwise just shows the notification icon
     */

    override fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter,context: Context) {

        Log.d("FinalDebug3","vf")
        db.collection("Users").document(UserProfileData.UserNumber).collection("currentChats")
            .addSnapshotListener(
                MetadataChanges.INCLUDE,
                EventListener<QuerySnapshot>{ snap, e ->
                    Log.d("datau","1 : "+AllChatDataModel.flagPersonalChat.toString())
                    AllChatDataModel.flagPersonalChat = !snap!!.metadata.hasPendingWrites()
                    Log.d("data","2: "+AllChatDataModel.flagPersonalChat.toString())
                    if(e != null){
                        Log.d("Error", "listen:error", e)
                        return@EventListener
                    }

                    for(dc in snap!!.documentChanges){

                        when(dc.type){
                            DocumentChange.Type.ADDED ->
                            {
                                Log.d("FinalDebug18" , "Added Called With ${dc} \n")
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var objectChatPersonal = ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()
                                objectChatPersonal.lastSeen = dc.document["lastSeen"].toString()
                                if(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId } == null)
                                {
                                    AllChatDataModel.personalChatList.add(objectChatPersonal)
                                   // AllChatDataModel.urlList[AllChatDataModel.personalChatList.size-1].
                                    //if()
                                }
                                if(AllChatDataModel.urlList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId } == null)
                                {
                                    fetchURLFromFirestore(objectChatPersonal.chatDocumentId,objectChatPersonal.otherNumber,presenter,context)
                                }
                                Log.d("FinalDebug4","all upate in added with ${AllChatDataModel.personalChatList.size}")
                                Log.d("FireStoreSnap",  dc.document["otherNumber"].toString())
                                Log.d("HomeActivity","Size"+AllChatDataModel.personalChatList.size.toString())
                            }

                            /**
                             * Document.modified is called wen a new message arrives
                             * This is because the last updated field of the chat document changes
                             * This first checks if the chat is already present or not
                             * If it is present, it replaces the chat object that is stored corresponding to that chat in [AllChatDataModel.personalChatList]
                             * Otherwise a new chat object is added to the Array List
                             */

                            DocumentChange.Type.MODIFIED ->
                            {
                               // Log.d("FinalDebug19" , "Modified called with ${dc} \n")
                                //AllChatDataModel.allChatArrayListPersonalStatic.clear()
                                var objectChatPersonal :ChatObject= ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()
                                objectChatPersonal.lastSeen = dc.document["lastSeen"].toString()
                                Log.d("FinalDebug22" , "Modified called with ${dc.document["otherNumber"]} \n")
                                if(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId } == null)
                                {
                                    AllChatDataModel.personalChatList.add(objectChatPersonal)
                                }
                                else
                                {
                                    AllChatDataModel.personalChatList.set(AllChatDataModel.personalChatList.indexOf(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId }) , objectChatPersonal)
                                }
                                Log.d("FinalDebug4","all upate${AllChatDataModel.personalChatList.size}")
                                Log.d("FireStoreSnap",  dc.document["otherNumber"].toString())
                                Log.d("HomeActivity","Size"+AllChatDataModel.personalChatList.size.toString())
                            }

                            DocumentChange.Type.REMOVED ->
                            {
                                Log.d("BeforeDeletion" , "Data : ${dc.document.data} \n Size : ${AllChatDataModel.personalChatList.size}}")
                                var objectChatPersonal :ChatObject= ChatObject()
                                objectChatPersonal.otherNumber=dc.document["otherNumber"].toString()
                                objectChatPersonal.chatDocumentId=dc.document["chatDocumentId"].toString()
                                objectChatPersonal.lastUpdated=dc.document["lastUpdated"].toString()
                                objectChatPersonal.lastSeen = dc.document["lastSeen"].toString()
                                AllChatDataModel.personalChatList.remove(AllChatDataModel.personalChatList.find { it.chatDocumentId == objectChatPersonal.chatDocumentId })
                                //presenter.sortPersonalChatList()
                                Log.d("AfterDeletion" , "Size : ${AllChatDataModel.personalChatList.size} \n Object : ${objectChatPersonal.otherNumber}")
                            }
                        }
                    }


                    Log.d("Run3","passNewMessagetoPrentercallled")

                    if(AllChatDataModel.flagPersonalChat) {
                        AllChatDataModel.flagPersonalChat=false
                        Log.d("FinalDebug5", " persona presenter.sortPersonalChatList() ${AllChatDataModel.personalChatList.size}")
                        presenter.sortPersonalChatList()
                    }

                })
    }

    private fun fetchURLFromFirestore(chatDocumentId: String, otherNumber: String,presenter: HomeActivityContract.Presenter,context: Context) {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `URLCollection` (`chatDocumentId` TEXT NOT NULL, `URL` TEXT NOT NULL, " +
                        "PRIMARY KEY(`chatDocumentId`))")
            }
        }
        roomdb = Room.databaseBuilder(context, AppDatabse::class.java, "Contact_Database").addMigrations(MIGRATION_1_2)
            .allowMainThreadQueries().build()
        try
        {
          var x = otherNumber.toDouble()
            //.jpeg
            FirebaseStorage.getInstance().reference.child(otherNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
                Log.d("RoomProfile","onSuccessOF Url FEtched personal")
                var urlInfo= URLInfo(chatDocumentId,it.toString())
                AllChatDataModel.urlList.add(urlInfo)
                roomdb!!.urlInfoDao().deleteAllURLData()
                roomdb!!.urlInfoDao().insertAllURLdata(urlInfo)
                presenter.sortPersonalChatList()


            }
        }
        catch (e :Exception)
        {
            FirebaseStorage.getInstance().reference.child("groupimages").child(chatDocumentId).downloadUrl.addOnSuccessListener {
                Log.d("RoomProfilcatcGroupe","onSuccessOF Url FEtched groupURL${it} ")
                var urlInfo= URLInfo(chatDocumentId,it.toString())
                AllChatDataModel.urlList.add(urlInfo)

                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE `URLCollection` (`chatDocumentId` TEXT NOT NULL, `URL` TEXT NOT NULL, " +
                                "PRIMARY KEY(`chatDocumentId`))")
                    }
                }
                roomdb = Room.databaseBuilder(context, AppDatabse::class.java, "Contact_Database").addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries().build()
                 roomdb!!.urlInfoDao().deleteAllURLData()
                roomdb!!.urlInfoDao().insertAllURLdata(urlInfo)
                presenter.sortPersonalChatList()


            }
        }

    }

    override fun retrieveURLFromRoom(presenter: HomeActivityContract.Presenter, context: Context) {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `URLCollection` (`chatDocumentId` TEXT NOT NULL, `URL` TEXT NOT NULL, " +
                        "PRIMARY KEY(`chatDocumentId`))")
            }
        }
        roomdb = Room.databaseBuilder(context, AppDatabse::class.java, "Contact_Database").addMigrations(MIGRATION_1_2)
            .allowMainThreadQueries().build()



          AllChatDataModel.urlList.addAll(roomdb!!.urlInfoDao().getAllURLInfo())

    }

    /**
     * This function gets the current statuses of all users who are on the app
     * Then if the mobile number of the user is in the contact list of the current user, then his status image is added to the
     * [maps] variable, which is a Hash Map of mobile number and corresponding status image.
     * This map is then passed to the adapter to display the information
     */

    override fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter, context: Context) {
        var maps = ArrayList<Pair<String , String>>()
        var model = ContactListModel()
        FirebaseFirestore.getInstance().collection("Users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.id == UserProfileData.UserNumber)
                        continue
                    if(! (ContactListModel().roomGetName(context, document.data.get("number").toString()) == document.data.get("number").toString()))
                        maps.add(Pair(model.roomGetName(context , document.data.get("number").toString()) , document.data.get("image").toString()))
                    //Log.d("Status" , "${maps[i].first} =>  ${maps[i].second}")
                }
                presenter.onStatusDataRecived(maps)
            }
    }

}