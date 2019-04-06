package com.example.litechat.view.activities

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListData
import com.example.litechat.model.NewDocumentCreate
import com.example.litechat.view.adapters.GroupContactAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_group_chat.*
import java.io.ByteArrayOutputStream
import java.time.Instant
import android.graphics.BitmapFactory
import com.example.litechat.model.contactsRoom.AppDatabse
import com.example.litechat.model.contactsRoom.URLInfo


class NewGroupChatActivity : AppCompatActivity() {
    private var groupName: String? = null
    private var newObj = NewDocumentCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_new_group_chat)

        var adapt = GroupContactAdapter(this)

        recyclerView2.apply {
            adapter = adapt
            layoutManager = LinearLayoutManager(this@NewGroupChatActivity)
        }

        buttong.setOnClickListener {
            groupName = num1.getText().toString()
            if (groupName != "") {
                if (ContactListData.groupContacts.size > 0) {
                    groupLoader.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    Log.d("check1", "onCLick")

                    for (i in 0 until ContactListData.groupContacts.size) {
                        AllChatDataModel.groupNumbers.add(ContactListData.groupContacts[i].mobileNumber)
                        Log.d("check", AllChatDataModel.groupNumbers.toString())
                    }

                    newObj.groupname = groupName
                    newObj.groupmembers.addAll(AllChatDataModel.groupNumbers)
                    newObj.groupmembers.add(AllChatDataModel.userNumberIdPM)
                    newObj.usernumber = AllChatDataModel.userNumberIdPM
                    Log.d("check5", newObj.toString())

                    // there may be a bug in this query same user creates group with same name might be a problem
                    database.collection("Chats").document().set(newObj).addOnSuccessListener {

                        database.collection("Chats").whereEqualTo("usernumber", AllChatDataModel.userNumberIdPM)

                            .whereEqualTo("groupname", groupName).get().addOnSuccessListener { document ->

                                for (doc in document) {
                                    Log.d("check2", doc.toString() + document.toString())
                                    var timeStamp = Instant.now().epochSecond
                                    for (i in 0 until AllChatDataModel.groupNumbers.size) {// since user group creter me do bar child createhora h
                                        var group = ChatObject()
                                        group.lastUpdated = timeStamp.toString()
                                        group.chatDocumentId = doc.id
                                        group.otherNumber = groupName
                                        group.lastSeen = timeStamp.toString()
                                        Log.d("check", group.toString())
                                        database.collection("Users").document(AllChatDataModel.groupNumbers[i])
                                            .collection("currentChats").document().set(group)
                                    }

                                    var me = ChatObject()
                                    me.lastUpdated = timeStamp.toString()
                                    me.chatDocumentId = doc.id
                                    me.otherNumber = groupName
                                    me.lastSeen = timeStamp.toString()
                                    database.collection("Users").document(AllChatDataModel.userNumberIdPM)
                                        .collection("currentChats").document().set(me)
                                        .addOnSuccessListener { result ->
                                            /* var intent = Intent(this@NewGroupChatActivity, ChatActivity::class.java)
                                            intent.putExtra("documentPathId", me.chatDocumentId)
                                            intent.putExtra("string", me.otherNumber)
                                            intent.putExtra("lastUpdated", me.lastUpdated)
                                            groupLoader.visibility = View.INVISIBLE
                                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                            AllChatDataModel.groupNumbers.clear()
                                            ContactListData.groupContacts.clear()
                                            startActivity(intent)
                                            finish()*/
                                            //by default save group image to Firebase
                                            saveGroupImageToFirebaseStorage(
                                                me.chatDocumentId,
                                                me.otherNumber,
                                                me.lastUpdated
                                            )


                                        }
                                }
                            }
                    }
                } else {

                    Toast.makeText(applicationContext, "Please select atleast 1 contact", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Please enter group name", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun saveGroupImageToFirebaseStorage(chatDocumentId: String, otherNumber: String, lastUpdated: String) {

        val storageRef = FirebaseStorage.getInstance().reference
        val bm = BitmapFactory.decodeResource(
            applicationContext.getResources(),
            com.example.litechat.R.drawable.group_icon_compressed
        )
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data1 = baos.toByteArray()
        Log.d("ImageGroupChatActivity18", "Upload begins")
        val uploadTask = storageRef.child("groupimages").child(chatDocumentId).putBytes(data1)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("ImageGroupChatActivity19", "Upload taslk on failure ${it}")
        }.addOnSuccessListener {
            Log.d("ImageGroupChatActivity20", "Image uploaded sucessfully Group compressed id :$chatDocumentId")

            FirebaseStorage.getInstance().reference.child("groupimages").child(chatDocumentId)
                .downloadUrl.addOnSuccessListener {

                val downloadURL = it.toString()

                Log.d(
                    "ImageGroupChatActivity25",
                    "Image uploaded  inside Fireget urlsucessfully Group compressed id :$chatDocumentId and URL$downloadURL"
                )
                AllChatDataModel.urlList.add(URLInfo(chatDocumentId, downloadURL))


                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE `URLCollection` (`chatDocumentId` TEXT,NOT NULL, `URL` TEXT, NOT NULL, " +
                                "PRIMARY KEY(`chatDocumentId`))")
                    }
                }
                val roomdb = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database").addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries().build()
                roomdb.urlInfoDao().insertAllURLdata(URLInfo(chatDocumentId, downloadURL))

                val intent = Intent(this@NewGroupChatActivity, ChatActivity::class.java)
                intent.putExtra("documentPathId", chatDocumentId)
                intent.putExtra("string", otherNumber)
                intent.putExtra("lastUpdated", lastUpdated)
                intent.putExtra("url", downloadURL)
                groupLoader.visibility = View.INVISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                AllChatDataModel.groupNumbers.clear()
                ContactListData.groupContacts.clear()
                startActivity(intent)
                finish()


            }

        }

    }
}

