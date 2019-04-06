package com.example.litechat.view.adapters

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.constraint.Placeholder
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.listeners.ListenerForFragmentChat
import com.example.litechat.R
import com.example.litechat.listeners.ListenerToPassString
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ChatObject
import com.example.litechat.model.ContactListModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.lang.Error
import java.lang.Exception
import java.net.URI
import java.time.Instant

class AdapterForFragmentChat(private var dataset :ArrayList<ChatObject>, private var context: Context,
                             private var listenerForFragmentChatImage: ListenerForFragmentChat, private var listenerForFragmentChatChat: ListenerForFragmentChat
): RecyclerView.Adapter<AdapterForFragmentChat.MyViewHolder>() {

    private  var listenerForProfileImage=ListenerToPassString()
    private  var listenerForGroupImage=ListenerToPassString()

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        var textView: TextView =view.findViewById(com.example.litechat.R.id.chatName)
        var imageView: ImageView=view.findViewById(com.example.litechat.R.id.profileImage)
        var greenDot: ImageView=view.findViewById(com.example.litechat.R.id.imageViewGreenDot)

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AdapterForFragmentChat.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.litechat.R.layout.fragment_all_chat, parent,false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    } // create a new view

    override fun getItemCount(): Int = dataset.size

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: AdapterForFragmentChat.MyViewHolder, position: Int) {
        /*
         * Since recycler view works in a way such that it only recycles the information that is displayed in the views
         * of the recycler view, images of groups are also visible as currently we had not specified what image to overwrite
         * the previous image
         */
        holder.textView.text = ContactListModel().roomGetName(context , dataset[position].otherNumber)
        Glide.with(context).clear(holder.imageView)
        Log.d("QueryF",dataset[position].otherNumber+ " \n" +position.toString())
        try {
            var t = dataset[position].otherNumber.toDouble()
            Log.d("RoomImage11" , "Entered try ${dataset[position].otherNumber}")

            /*// previously loading image by downloading URL
            FirebaseStorage.getInstance().reference.child(dataset[position].otherNumber).child("ProfileImage").downloadUrl.addOnSuccessListener {
                Log.d("Firebase" , "Entered and retrieved storage successfully ${it}")
                // onLoad Started ki wajah se profile image not being displayed in fragment chat
                try {

                    Glide.with(context).load(it.toString()).apply(RequestOptions().placeholder(context.getDrawable(R.drawable.profile))).
                        into(holder.imageView)

                }catch (e : Error)
                {
                    Log.d("Crash" , e.stackTrace.toString())
                }

            }*/

            // saving profile images in private directory
        /*    listenerForProfileImage.setCustomObjectListener(object : ListenerToPassString.Listener
            {
                override fun onDataRecieved(uri: String) {
                    Log.d("ImageProfile15", "Glide ke apss ondatarecived ke andar Me")
                    Glide.with(context).load(uri).into(holder.imageView)
                   // holder.imageView.setImageURI(Uri.parse(uri))
                }
            })

            if(searchForImageFirst(dataset[position].otherNumber,dataset[position].chatDocumentId))
            {

                Log.d("ImageProfileFragChat","image loaded from storage")
                val output2 = getPrivateAlbumStorageDir(context,"LiteChat_ProfileImage")
                if (!output2!!.exists()) {
                    Log.d("ImageProfile18", "Ouput2 exists me" + output2.mkdirs())
                }

                Log.d("ImageProfile", "Ouput2 exits me" + output2.exists())

                val localFile2 = File(output2, "IMG_${dataset[position].chatDocumentId}_${dataset[position].otherNumber}.jpeg")
                var uri = Uri.parse(localFile2.path)
                Log.d("ImageProfile","Uri for ImageProfile$uri")
                Log.d("ImageProfile","ImageLoadedfromStorage")
                Glide.with(context).load(uri).into(holder.imageView)
               // holder.imageView.setImageURI(uri)
            }

            else
            {   Log.d("ImageProfile122", "image dowwnload to be started")
                saveImageFromFirebaseToDevice(dataset[position].otherNumber,listenerForProfileImage,
                    "${dataset[position].otherNumber}/" + "ProfileImage",dataset[position].chatDocumentId)
            }*/

           // var x = AllChatDataModel.urlList.get(AllChatDataModel.urlList.indexOf(AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId }))

           // Log.e("RoomImage12","AdapterPersonal URL ${(AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL}")
            if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                Glide.with(context)
                    .apply { RequestOptions().placeholder(R.drawable.profile) }
                    .load((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL)
                    .into(holder.imageView)
               Log.d("RoomImage13", "if of personal + ${dataset[position].otherNumber}")
            }
            else
            {
                Glide.with(context).load(R.drawable.profile).into(holder.imageView)
                Log.d("RoomImage14", "else of personal + ${dataset[position].otherNumber}")
            }
        }catch (e : Exception)
        {
            Log.e("RoomImage15" , "Entered catch \n ${e} with ${dataset[position].otherNumber}")

         /*   //previouly for diaplying group icons
          FirebaseStorage.getInstance().reference.child("groupimages").child(dataset[position].chatDocumentId).downloadUrl.addOnSuccessListener {
              Glide.with(context)
                  .load(it)
                  .apply(RequestOptions().placeholder(context.getDrawable(R.drawable.ic_group)))
                  .into(holder.imageView)

            }*/

            // displaying images from storage
            /*listenerForGroupImage.setCustomObjectListener(object : ListenerToPassString.Listener
            {
                override fun onDataRecieved(uri: String) {
                    Log.d("ImageGroup15", "Glide ke apss ondatarecived ke andar Me")
                  Glide.with(context).load(uri).into(holder.imageView)
                  // holder.imageView.setImageURI(Uri.parse(uri))
                }
            })

            if(searchForImageFirst(dataset[position].otherNumber,dataset[position].chatDocumentId))
            {


                val output2 = getPrivateAlbumStorageDir(context,"LiteChat_ProfileImage")
                if (!output2!!.exists()) {
                    Log.d("ImageGroup18", "Ouput2 exists me" + output2.mkdirs())
                }

                Log.d("ImageGroup19", "Ouput2 exits me" + output2.exists())

                val localFile2 = File(output2, "IMG_${dataset[position].chatDocumentId}_${dataset[position].otherNumber}.jpeg")
                var uri = Uri.parse(localFile2.path)
                Log.d("ImageGroup19","Uri for ImageProfile$uri")
                Log.d("ImageGroup","ImageLoadedfromStorage")
                Glide.with(context).load(uri).into(holder.imageView)
               // holder.imageView.setImageURI(uri)
            }

            else
            {   Log.d("ImageGroup122", "image dowwnload to be started")
                saveImageFromFirebaseToDevice(dataset[position].otherNumber,listenerForProfileImage,
                    "groupimages/${dataset[position].chatDocumentId}",dataset[position].chatDocumentId)// .jpeg
            }*/
            if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                Glide.with(context)
                    .apply { RequestOptions().placeholder(R.drawable.profile) }
                    .load((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL)
                    .into(holder.imageView)
                Log.d("RoomImage16", "if of group + ${dataset[position].otherNumber}")
            }
                else
            {
                Glide.with(context).load(R.drawable.ic_group).into(holder.imageView)
                Log.d("RoomImage17", "else of group + ${dataset[position].otherNumber}")

            }
        }

        Log.d("FinalDebug11","AllChatDataModel.personalChatList.size:${AllChatDataModel.personalChatList.size}\n${AllChatDataModel.personalChatList.contains(dataset[position])}")
        /*Log.d("TestNotif" , "position = ${position} \n LastSeen  = ${AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen} " +
                "\n  DocumentId = ${dataset[position].chatDocumentId} \n LastUpdated = ${AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastUpdated}")*/
        //AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < Instant.now().epochSecond.toString() &&
        if (AllChatDataModel.personalChatList.size!=0 && (AllChatDataModel.personalChatList.find { it.chatDocumentId==dataset[position].chatDocumentId }!=null) && AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < Instant.now().epochSecond.toString() && AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastSeen < AllChatDataModel.personalChatList.find { it.chatDocumentId == dataset[position].chatDocumentId }!!.lastUpdated)
        {
            holder.greenDot.visibility = View.VISIBLE
        }else{
            holder.greenDot.visibility = View.INVISIBLE
        }

        holder.textView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                Log.d("AllChatNumber" , AllChatDataModel.userNumberIdPM)

                Log.d("Debug13" , AllChatDataModel.personalChatList.size.toString())
                Log.d("Data","first time")
                Log.d("Person",holder.textView.text.toString())
               var urlToPass = " "
                if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                    urlToPass = (AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL
                    Log.d("RoomImage21", "if of onclick + ${dataset[position].otherNumber}")
                }
                else
                {

                    Log.d("RoomImage14", "else of onclick+ ${dataset[position].otherNumber}")
                }
               listenerForFragmentChatChat.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated, urlToPass)
                // listener to send number for activity
            }
        })

        holder.imageView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
            Log.d("GroupInfo2","documentId from adapter${dataset[position].chatDocumentId}")
                // give number to launch profile
                var urlToPass = " "
                if((AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!= null) {
                    urlToPass = (AllChatDataModel.urlList.find { it.chatDocumentId == dataset[position].chatDocumentId })!!.URL
                    Log.d("RoomImage21", "if of onclick + ${dataset[position].otherNumber}")
                }
                else
                {

                    Log.d("RoomImage14", "else of onclick+ ${dataset[position].otherNumber}")
                }
                listenerForFragmentChatImage.listener!!.onDataRecieved(dataset[position].otherNumber,dataset[position].chatDocumentId,dataset[position].lastUpdated,urlToPass)
            }
        })
    }



    private fun saveImageFromFirebaseToDevice(otherNumber: String, listenerToPassString: ListenerToPassString,path:String, chatDocumentId: String)
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
            val output =getPrivateAlbumStorageDir(context,"LiteChat_ProfileImage")
            if (!output!!.exists()) {
                Log.d("ImageSharing16", "Ouput exists" + output.mkdirs())
            }


            Log.d("ImageSharing15", "Ouput exits" + output.exists())

            val localFile = File(output, "IMG_${chatDocumentId}_$otherNumber.jpeg")

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

                    Log.d("ImageSharing12", "download:SUCCESS");

                    var uri = Uri.parse(localFile.path)
                    Log.d("ImageSharing16", "download:SUCCESS and URI : $uri path :${localFile.path}");

                    /*try{
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
                    }*/

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

    /*override fun onViewRecycled(holder: MyViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(context).clear(holder.imageView)
        holder.imageView.setImageResource(R.drawable.profile)
    }*/
   private  fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun searchForImageFirst(otherNumber: String,chatDocumentId:String): Boolean
    {
        val output = getPrivateAlbumStorageDir(context,"LiteChat_ProfileImage")
        if (!output!!.exists()) {
            return false
        }

        else
        {
            val localFile = File(output, "IMG_${chatDocumentId}_$otherNumber.jpeg")
            if (!localFile.exists())
                return false
        }


        return true

    }

   private  fun getPrivateAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Log.e("ProfileImageDisplay1", "Directory not created")
        }
        return file
    }

   /* fun applyOptions(Context context, GlideBuilder builder) {
    int bitmapPoolSizeBytes = 1024 * 1024 * 0; // 0mb
    int memoryCacheSizeBytes = 1024 * 1024 * 0; // 0mb
    builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
    builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));*/
/*}*/

}