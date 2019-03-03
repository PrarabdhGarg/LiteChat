package com.example.litechat.view.activities

import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_personal_chat.*
import java.lang.reflect.Field
import java.time.Instant

class NewPersonalChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_personal_chat)

        val db = FirebaseFirestore.getInstance()

        buttonNewPersonalChat.setOnClickListener(object : View.OnClickListener {

            @RequiresApi(Build.VERSION_CODES.O)
                   override fun onClick(v: View?)
            {
                // var newNumber= editTextNewNumber.text.toString().trim()
                if (editTextNewNumber.text.toString().trim().isEmpty())
                {
                    Toast.makeText(this@NewPersonalChatActivity, "Enter A valid Mobile Number ", Toast.LENGTH_LONG)
                        .show()
                }
                else
                   {
                         //Method to check if current Personal  Chats exists or not
                         // in Current Logged in user
                       // loader start
                        db.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats"). //
                         whereEqualTo("otherNumber",editTextNewNumber.text.toString().trim()).get().addOnSuccessListener { documents ->
                         // if chat between these two existed previously than open their chat
                            Log.d("New1","g")
                         if(!documents.isEmpty )
                         {
                             Log.d("New2",documents.toString())
                             for(doc in documents)
                             {
                                 Log.d("New3",doc.toString())
                                 var currentPersonalChatData: ChatObject = doc.toObject(ChatObject::class.java)
                                 var intent = Intent(this@NewPersonalChatActivity, ChatActivity::class.java)

                                 intent.putExtra("documentPathId", currentPersonalChatData.chatDocumentId)
                                 intent.putExtra("string", currentPersonalChatData.otherNumber)
                                 intent.putExtra("lastUpdated",currentPersonalChatData.lastUpdated)
                                 intent.putExtra("lastSeen" , currentPersonalChatData.lastSeen)
                                 startActivity(intent)
                                 finish()
                             }// loser fnsh
                        }
                        else
                        { //
                            Log.d("New4",documents.toString())
                             // if no previous chat exists than start a new chat by creating
                             // appropriate  fields in database
                            var newNumber=editTextNewNumber.text.toString().trim()
                            var objNewChatData=NewDocumentForChat(AllChatDataModel.userNumberIdPM,newNumber) //create a document with number1 nd number2 fields

                            db.collection("Chats").document().set(objNewChatData).addOnSuccessListener {

                                db.collection("Chats").whereEqualTo("number1",AllChatDataModel.userNumberIdPM).// get this value from user

                                    whereEqualTo("number2",newNumber).get().addOnSuccessListener { result ->

                                    if(result != null)
                                    {
                                        Log.d("New5",result.toString())
                                         // if document created suceessfully
                                         for(doc in result)
                                         {
//                                       /*  *
                                             Log.d("New6",doc.toString())
//                                          * Check all sucees listeners
//                                          * */
                                             Toast.makeText(this@NewPersonalChatActivity, "Enter", Toast.LENGTH_LONG).show()
                                             Log.d("Enter", doc.id)

                                             var timeStamp = Instant.now().epochSecond

                                             var chatObject2 = ChatObject()
                                             chatObject2.lastUpdated = timeStamp.toString()
                                             chatObject2.chatDocumentId = doc.id
                                             chatObject2.otherNumber = AllChatDataModel.userNumberIdPM
                                             chatObject2.lastSeen = timeStamp.toString() // add numbers
                                             db.collection("Users").document(newNumber)
                                                 .collection("currentChats").document().set(chatObject2)
                                                 .addOnSuccessListener {

                                                     /**
                                                      * check non null
                                                      * */

                                                     var chatObject1 = ChatObject()
                                                     chatObject1.lastUpdated = timeStamp.toString()
                                                     chatObject1.chatDocumentId = doc.id
                                                     chatObject1.otherNumber = newNumber
                                                     chatObject1.lastSeen = timeStamp.toString()// add numbers
                                                     db.collection("Users").document(AllChatDataModel.userNumberIdPM)
                                                         .collection("currentChats").document().set(chatObject1)
                                                         .addOnSuccessListener { res ->

                                                             // check non null
                                                             var intent = Intent(this@NewPersonalChatActivity, ChatActivity::class.java)
                                                             intent.putExtra("documentPathId",chatObject1.chatDocumentId)
                                                             intent.putExtra("string", chatObject1.otherNumber)
                                                             intent.putExtra("lastUpdated",chatObject1.lastUpdated)
                                                             startActivity(intent)
                                                             // Loader End
                                                             finish()

                                                         }
                                                 }


                                         }


                                    }



                                }



//                              /**
//                               * dlete childs if message not sent
//                               * */


                            }  //
                        }    //
                    }
               }

            }
        })
    }

     class NewDocumentForChat(number1: String,number2:String){

         var number1=number1
         var number2=number2

     }
}

