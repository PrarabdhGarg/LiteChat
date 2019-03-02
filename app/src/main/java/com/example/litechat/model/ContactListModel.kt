package com.example.litechat.model

import android.arch.persistence.room.Room
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.model.contactsRoom.AppDatabse
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.view.activities.NewPersonalChatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant

class ContactListModel : ContactFragContract.Model {
    override fun roomGetName(applicationContext: Context, number: String) : String {
        Log.d("Context" , applicationContext.toString())
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()
        var t = ""

        t = db.userDao().getName(number)

        if(t == null)
        {
            t = number
        }
        return t
    }

    override fun roomDeleteData(applicationContext: Context) {
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()
        db.userDao().deleteAllData()
    }


    override fun roomSetData(applicationContext: Context, userList: List<User>) {
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()

        for (item in userList) {

            db.userDao().insertContact(item)
        }

    }

    override fun roomGetData(applicationContext: Context): List<User> {

        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()

        return db.userDao().getContactList()

    }


    override fun getFirebaseData() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun startChatActivity(number: String, presenter: ContactFragContract.Presenter) {

        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(AllChatDataModel.userNumberIdPM)
            .collection("currentChats").whereEqualTo("otherNumber", number).get()
            .addOnSuccessListener { documents ->
                // if chat between these two existed previously than open their chat
                Log.d("New1", "g")
                if (!documents.isEmpty) {
                    Log.d("New2", documents.toString())
                    for (doc in documents) {
                        Log.d("New3", doc.toString())
                        presenter.passDataForChatActivity(doc.toObject(ChatObject::class.java))
                    }
                } else {
                    Log.d("New4", documents.toString())
                    // if no previous chat exists than start a new chat by creating
                    // appropriate  fields in database
                    //var newNumber=number
                    //var objNewChatData= NewPersonalChatActivity.NewDocumentForChat(AllChatDataModel.userNumberIdPM, newNumber) //create a document with number1 nd number2 fields

                    db.collection("Chats").document().set(
                        NewPersonalChatActivity.NewDocumentForChat
                            (AllChatDataModel.userNumberIdPM, number)
                    )
                        .addOnSuccessListener {

                            db.collection("Chats").whereEqualTo("number1", AllChatDataModel.userNumberIdPM)
                                .
                                    whereEqualTo("number2", number).get().addOnSuccessListener { result ->

                                    if (result != null) {
                                        Log.d("New5", result.toString())
                                        // if document created suceessfully
                                        for (doc in result) {
//                                       /*  *
                                            Log.d("New6", doc.toString())
//                                          * Check all sucees listeners
//                                          * */
                                            //  Toast.makeText(this@NewPersonalChatActivity, "Enter", Toast.LENGTH_LONG).show()
                                            Log.d("Enter", doc.id)

                                            var timeStamp = Instant.now().epochSecond

                                            var chatObject2 = ChatObject()
                                            chatObject2.lastUpdated = timeStamp.toString()
                                            chatObject2.chatDocumentId = doc.id
                                            chatObject2.otherNumber = AllChatDataModel.userNumberIdPM
                                            chatObject2.lastSeen = timeStamp.toString()// add numbers
                                            db.collection("Users").document(number)
                                                .collection("currentChats").document().set(chatObject2)
                                                .addOnSuccessListener {

                                                    /**
                                                     * check non null
                                                     * */

                                                    var chatObject1 = ChatObject()
                                                    chatObject1.lastUpdated = timeStamp.toString()
                                                    chatObject1.chatDocumentId = doc.id
                                                    chatObject1.otherNumber = number
                                                    chatObject1.lastSeen = timeStamp.toString()// add numbers
                                                    db.collection("Users").document(AllChatDataModel.userNumberIdPM)
                                                        .collection("currentChats").document().set(chatObject1)
                                                        .addOnSuccessListener { res ->

                                                            presenter.passDataForChatActivity(chatObject1)
                                                            // check non null
                                                            /*   var intent = Intent(this@NewPersonalChatActivity, ChatActivity::class.java)
                                                            intent.putExtra("documentPathId",chatObject1.chatDocumentId)
                                                            intent.putExtra("string", chatObject1.otherNumber)
                                                            intent.putExtra("lastUpdated",chatObject1.lastUpdated)
                                                            startActivity(intent)
                                                            // Loader End
                                                            finish()*/

                                                        }
                                                }


                                        }


                                    }


                                }

                        }
                }
            }


    }

}

