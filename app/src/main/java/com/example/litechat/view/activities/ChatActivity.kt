
package com.example.litechat.view.activities

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log

import android.view.View
import android.view.WindowManager

import kotlinx.android.synthetic.main.activity_chat.*

import android.widget.ViewAnimator
import java.lang.Double.parseDouble

import kotlinx.android.synthetic.main.activity_chat.*
import android.widget.Toast

import com.example.litechat.contracts.ChatContract
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.MessageModel
import com.example.litechat.presenter.ChatPresenter
import com.example.litechat.view.adapters.AdapterForChatActivity
import java.time.Instant
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.AppDatabse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.lang.Double


class ChatActivity : AppCompatActivity(), ChatContract.CView {
    private  val REQUEST_IMAGE_GET = 1
    private var myDataset= ArrayList<MessageModel>()
    private var numeric = true
    val  ref = FirebaseStorage.getInstance().reference
    private lateinit var adapterForChatActivity: AdapterForChatActivity
    private var chatPresenter = ChatPresenter(this)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.litechat.R.layout.activity_chat)

        adapterForChatActivity= AdapterForChatActivity(myDataset,applicationContext)
        recyclerView.apply {
            adapter=adapterForChatActivity
            setHasFixedSize(true)
        }

        // To store chat info passed from Fragment Chat
         AllChatDataModel.otherUserNumber = intent.getStringExtra("string")
         AllChatDataModel.documentPathId=intent.getStringExtra("documentPathId")
         AllChatDataModel.lastUpdated=intent.getStringExtra("lastUpdated")
         var lastSeen = intent.getStringExtra("lastSeen")
         //Log.e("LastSeen" , lastSeen)
        textViewOtherUser.setOnClickListener(View.OnClickListener {
            try
            {
                val num = Double.parseDouble( AllChatDataModel.otherUserNumber)
            }
            catch (e: NumberFormatException)
            {
                numeric = false
            }

            if(numeric)
            {   //  to show contact name of person chatting with
                var intent = Intent(this,ProfileOtherUser::class.java)
                intent.putExtra("number" , AllChatDataModel.otherUserNumber)
                startActivity(intent)
            }
            else
            {
                var intent= Intent(this,GroupInfoActivity::class.java)
              //  Log.d("GroupInfo1","documentPathId$chat")
                intent.putExtra("documentPathId",AllChatDataModel.documentPathId)
                startActivity(intent)
            }

        })
        //Update last seen of the user for current chat
         FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).collection("currentChats").whereEqualTo("chatDocumentId" , AllChatDataModel.documentPathId).get().addOnSuccessListener {
             for (i in it)
             {
                 i.reference.update("lastSeen" , Instant.now().epochSecond.toString())
             }
         }

        // to differentiate between group and personal chat
        try
        {
            var num= AllChatDataModel.otherUserNumber.toDouble()
        }catch (e : java.lang.Exception){
            numeric = false

        }
        if(numeric)
        {   //To show profile image and name of the other user in current chat
            textViewOtherUser.text = searchContactName(AllChatDataModel.otherUserNumber)
            FirebaseStorage.getInstance().reference.child(AllChatDataModel.otherUserNumber).child("ProfileImage")
              .downloadUrl.addOnSuccessListener { uri ->
              Glide.with(applicationContext).load(uri).into(imageViewOtherPerson)
            }
        }
        else
        {
            //to show group name
            textViewOtherUser.text = AllChatDataModel.otherUserNumber
            // for group icon
           /* FirebaseStorage.getInstance().getReference().child(AllChatDataModel.otherUserNumber).child("ProfileImage")
                .downloadUrl.addOnSuccessListener { uri ->

                Glide.with(applicationContext).load(uri).into(imageViewOtherPerson)
            }*/
        }

        //get old messages of current chat
        chatPresenter.getNewOtherMessagesFromInteractor()

        // to send messages
        buttonSend.setOnClickListener {
                // also change last updated
                if(AllChatDataModel.documentPathId != null)
                {
                    if (!editTextSend.text.toString().isEmpty())
                    {
                        var messageModel= MessageModel()
                        messageModel.message=editTextSend.text.toString()
                        messageModel.sentBy=AllChatDataModel.userNumberIdPM// sala ab bhi null h
                        messageModel.sentOn=Instant.now().epochSecond.toString()
                        editTextSend.setText("")
                        buttonSend.isClickable=false
                        chatPresenter.passNewSetMessageFromViewtoPresenter(messageModel,applicationContext)
                    }
                    else
                    {
                        Toast.makeText(this@ChatActivity,"Please type a message to send",Toast.LENGTH_SHORT).show()
                    }
                }

        }

        // button for image sharing
        buttonCamera.setOnClickListener {
Toast.makeText(this,"Disable Window",Toast.LENGTH_SHORT).show()
selectImage()




        }


    }

     private  fun selectImage() {
         val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
             type = "image/*"
         }
         if (intent.resolveActivity(packageManager) != null) {
             startActivityForResult(intent, REQUEST_IMAGE_GET)
         }
     }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
           // val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            val fullPhotoUri: Uri = data!!.data
            // Do work with photo saved at fullPhotoUri
            imageViewOtherPerson.setImageURI(fullPhotoUri)

            uploadImage(fullPhotoUri)
        }
    }

    // method to upload image

    private fun uploadImage(fullPhotoUri: Uri)
    {

        val  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos)
        val data1 = baos.toByteArray()
        val timeStamp =Instant.now().epochSecond.toString()
        val uploadTask =  ref.child("ImageSharing").child(AllChatDataModel.documentPathId).child(timeStamp).putBytes(data1)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.d("Firebase Storage" , "Image uploaded sucessfully")
            //Glide.with(applicationContext).load(fullPhotoUri).into(ProfileImageView).onLoadStarted(getDrawable(R.drawable.profile))
            updateProfileImageOnDatabase(timeStamp)
        }


    }

    // method to update sent message via image
    private fun updateProfileImageOnDatabase(timestamp : String)
    {

        ref.child("ImageSharing").child(AllChatDataModel.documentPathId).child(timestamp).downloadUrl.addOnSuccessListener {
            val  downloadUrl = it.toString()
            if (!downloadUrl.isEmpty())
            {
                val messageModelImage= MessageModel()
                messageModelImage.message="~$^*/"+downloadUrl
                messageModelImage.sentBy=AllChatDataModel.userNumberIdPM// sala ab bhi null h
                messageModelImage.sentOn=Instant.now().epochSecond.toString()
                chatPresenter.passNewSetMessageFromViewtoPresenter(messageModelImage,applicationContext)
            }
            else
            {
                Toast.makeText(this@ChatActivity,"Please select a image to send",Toast.LENGTH_SHORT).show()
            }
        }

    }




    // method to search contact
    private fun searchContactName(number: String): String {

        var name : String
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()
        try
        {

            name= db.userDao().getName(AllChatDataModel.otherUserNumber)
            Log.d("debug",name)
            return name

        }

        catch (e: Exception){
            Log.d("debug",e.toString())
            return  number
        }



    }

    override fun getOtherMessagesFromPresenter() {

    }

    override fun displayMessage()
    {
            Log.d("Run4","code of displayNewMessage")
          // claer previous messages from adapeter datset to avoid appending of messages
           myDataset.clear()
           myDataset.addAll(AllChatDataModel.allChatArrayListPersonalStatic)

           Log.d("Run7",myDataset.size.toString())
           adapterForChatActivity.notifyDataSetChanged()
       if(myDataset.size>2)
       {recyclerView.smoothScrollToPosition(myDataset.size-1)}
           buttonSend.isClickable=true
       }

    override fun onBackPressed()
    {
        FirebaseFirestore.getInstance().collection("Users").document(UserProfileData.UserNumber).collection("currentChats").whereEqualTo("chatDocumentId" , AllChatDataModel.documentPathId).get().addOnSuccessListener {
            for (i in it)
            {
                i.reference.update("lastSeen" , Instant.now().epochSecond.toString())
            }
            AllChatDataModel.allChatArrayListPersonalStatic.clear()
            AllChatDataModel.flagOnBackPressed = true
            chatPresenter.notifyModelOfBackPressed()
            super.onBackPressed()
        }

    }
 }




