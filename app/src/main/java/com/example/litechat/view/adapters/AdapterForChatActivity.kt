package com.example.litechat.view.adapters

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ContactListModel
import com.example.litechat.model.MessageModel
import com.example.litechat.model.UserProfileData
import android.media.MediaScannerConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioTimestamp
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.listeners.ListenerForFragmentChat
import com.example.litechat.listeners.ListenerToPassString
import com.example.litechat.view.activities.ChatActivity
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class AdapterForChatActivity(private var dataset:ArrayList<MessageModel>,private var context: Context,private var activity:ChatActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val storage = FirebaseStorage.getInstance()
    private var listenerForLoadingImageMe = ListenerToPassString()
    private var listenerForLoadingImageYou = ListenerToPassString()

    class MyViewHolderMe(val view: View): RecyclerView.ViewHolder(view){


        var myName:TextView=view.findViewById(R.id.myName)
        var myMessage:TextView=view.findViewById(R.id.myMessage)
        var myImageShare:ImageView=view.findViewById(R.id.myImageShare)

    }

    class MyViewHolderYou(val view: View): RecyclerView.ViewHolder(view){


        var youName:TextView=view.findViewById(R.id.youName)
        var youMessage:TextView=view.findViewById(R.id.youMessage)
        var youImageShare:ImageView=view.findViewById(R.id.youImageShare)

    }


    override fun getItemViewType(position: Int): Int {
        if(dataset[position].sentBy.equals(AllChatDataModel.userNumberIdPM))

        {
            return 0
        }
        else{

            return 1
        }

//        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view:View?
        //Here user no. is to be placed
        if(position==0)

        {

             view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat_me, parent,false) as View

            return MyViewHolderMe(view)
        }
       else{

             view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat_you, parent,false) as View
            return MyViewHolderYou(view)
        }


    } // create a new view


    override fun getItemCount(): Int {


        return dataset.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType==0)
        {    val holderMe: MyViewHolderMe= holder as MyViewHolderMe
            holderMe.myName.visibility=View.VISIBLE
            if(position>0&&dataset[position].sentBy==dataset[position-1].sentBy)
                holderMe.myName.visibility=View.GONE
            else
                holderMe.myName.text=(UserProfileData.UserName)

            if(dataset[position].message.length>=5&&dataset[position].message.substring(0,5)=="~$^*/")
            {
                listenerForLoadingImageMe.setCustomObjectListener(object : ListenerToPassString.Listener
                {
                    override fun onDataRecieved(string: String) {
                        Log.d("ImageSharing15", "Glide ke apss ondatarecived ke andar Me")
                        Glide.with(context).load(string).apply(RequestOptions().placeholder(R.drawable.placeholder_image_sharing)).into(holderMe.myImageShare)
                    }
                })


                Log.d("Position",position.toString())
                Log.d("ImageSharing2Me", "timeStamp ie name of image "+dataset[position].message)
                holderMe.myMessage.visibility=View.GONE
                holderMe.myImageShare.visibility=View.VISIBLE

                if(searchForImageFirst(dataset[position].message.substring(5)))
                {
                    val output2 = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LiteChat")
                    if (!output2.exists()) {
                        Log.d("ImageSharing18", "Ouput2 exists me" + output2.mkdirs())
                    }

                    Log.d("ImageSharing19", "Ouput2 exits me" + output2.exists())

                    val localFile2 = File(output2, "IMG_${AllChatDataModel.documentPathId}_${dataset[position].message.substring(5)}.jpg")
                    var uri = Uri.parse(localFile2.path)
                    Log.d("ImageSharing19","Uri for myImageShare$uri")
                    Glide.with(context).load(uri.toString()).apply(RequestOptions().placeholder(R.drawable.placeholder_image_sharing)).into(holderMe.myImageShare)
                }

                else
                    saveImageFromFirebaseToDevice(dataset[position].message.substring(5),listenerForLoadingImageMe)
                // Glide.with(context).load(dataset[position].message.substring(5)).into(holderMe.myImageShare)
                //passing timeStamp as name of image


            }

            else
            {
            Log.d("Position",position.toString())
            holderMe.myImageShare.visibility=View.GONE
            holderMe.myMessage.text=(dataset[position].message)
            }
        }
        else
        {
            val holderYou: MyViewHolderYou=holder as MyViewHolderYou
            holderYou.youName.visibility=View.VISIBLE
            listenerForLoadingImageYou.setCustomObjectListener(object : ListenerToPassString.Listener
            {
                override fun onDataRecieved(string: String) {
                    Log.d("ImageSharing15", "Glide ke apss ondatarecived ke andar You")
                    Glide.with(context).load(string).apply(RequestOptions().placeholder(R.drawable.placeholder_image_sharing)).into(holderYou.youImageShare)
                }
            })


            if(position>0&&dataset[position].sentBy==dataset[position-1].sentBy)
                holderYou.youName.visibility=View.GONE
            else
                holderYou.youName.text=(ContactListModel().roomGetName(context , dataset[position].sentBy.toString()))

            if(dataset[position].message.length>=5&&dataset[position].message.substring(0,5)=="~$^*/")
            {

                Log.d("Positione",position.toString())
                Log.d("ImageSharing2You", "timeStamp ie name of image "+dataset[position].message)
                holderYou.youMessage.visibility=View.GONE
                holderYou.youImageShare.visibility=View.VISIBLE

                if(searchForImageFirst(dataset[position].message.substring(5)))
                {
                    Log.d("ImageSharingFinal","searchforImageFirstReturns true")
                    val output2 = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LiteChat")
                    if (!output2.exists()) {
                        Log.d("ImageSharing18", "Ouput2 exists" + output2.mkdirs())
                    }

                    Log.d("ImageSharing19", "Ouput2 exits" + output2.exists())

                    val localFile2 = File(output2, "IMG_${AllChatDataModel.documentPathId}_${dataset[position].message.substring(5)}.jpg")
                    var uri = Uri.parse(localFile2.path)
                    Log.d("ImageSharing19","Uri for youImageShare$uri")
                    Glide.with(context).load(uri.toString()).apply(RequestOptions().placeholder(R.drawable.placeholder_image_sharing)).into(holderYou.youImageShare)
                }

                else {
                    Log.d("ImageSharingFinal","searchforImageFirstReturns false")
                    saveImageFromFirebaseToDevice(dataset[position].message.substring(5), listenerForLoadingImageYou)
                }

               // Glide.with(context).load(dataset[position].message.substring(5)).into(holderYou.youImageShare)
                // passing timeStamp as name of image




                Log.e("NumberCheck" ,dataset[position].sentBy.toString())

            }

            else
            {
                Log.d("Positione",position.toString())
                holderYou.youImageShare.visibility=View.GONE
                holderYou.youMessage.text=(dataset[position].message)
                Log.e("NumberCheck" ,dataset[position].sentBy.toString())

            }

        }
    }


    fun saveImageFromFirebaseToDevice(timeStamp: String, listenerToPassString: ListenerToPassString)
    {
        Log.d("ImageSharingFinal","Enter save Iamge from firsbase")
        // Check if storage is writable
        Log.d("ImageSharing8",isExternalStorageWritable().toString())

        //checkPermissionGranted()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("ImageSharing9","Permission is not granted")
        }
        else
            Log.d("ImageSharing9","Permission is  granted")


        try {
            // Make directory named litechat
            val output = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LiteChat")
            if (!output.exists()) {
                Log.d("ImageSharing16", "Ouput exists" + output.mkdirs())
            }


            Log.d("ImageSharing15", "Ouput exits" + output.exists())

            val localFile = File(output, "IMG_${AllChatDataModel.documentPathId}_$timeStamp.jpg")

            val mStorage = FirebaseStorage.getInstance("gs://litechat-3960c.appspot.com")
            val mStorageRef = mStorage.getReference()


            val downloadRef = mStorageRef.getRoot()
                .child("ImageSharing/" + AllChatDataModel.documentPathId + "/" + timeStamp + ".jpg");
            // Download and get total bytes
            downloadRef.getFile(localFile)
                /*.addOnProgressListener{

                        showProgressNotification(1,title, "",
                            taskSnapshot.getBytesTransferred(),
                            taskSnapshot.getTotalByteCount());

                }*/
                .addOnSuccessListener {

                    Log.d("ImageSharing12", "download:SUCCESS");

                    var uri = Uri.parse(localFile.path)
                    Log.d("ImageSharing16", "download:SUCCESS and URI : $uri path :${localFile.path}");

                   try{
                       Log.d("ImageSharing21","Entering Media Scanner")
                       MediaScannerConnection.scanFile(
                        context,
                        arrayOf(localFile.getAbsolutePath()),
                        null,
                        object : MediaScannerConnection.OnScanCompletedListener {
                            override   fun onScanCompleted(path: String, uri: Uri) {
                                Log.i("ExternalStorage", "Scanned $path:")
                                Log.i("ExternalStorage", "-> uri=$uri")
                            }
                        })
                }
            catch (e: Exception) {
                Log.d("ImageSharing8","IoException catch of media scanner")
                throw IOException()
            }

                    listenerToPassString.listener!!.onDataRecieved(uri.toString())
                }
                .addOnFailureListener {
                    Log.d("ImageSharing13", "onFailureTry2 +  " + it.toString())
                }

        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }

        /*
        val mediaFile =  File(dir.path + File.separator +
        "IMG_"+ AllChatDataModel.documentPathId+timeStamp + ".jpg");*/

    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun searchForImageFirst(timeStamp: String): Boolean
    {
        val output = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LiteChat")
        if (!output.exists()) {
           return false
        }

        else
        {
            val localFile = File(output, "IMG_${AllChatDataModel.documentPathId}_$timeStamp.jpg")
            if (!localFile.exists())
                return false
        }


        return true

    }

}
