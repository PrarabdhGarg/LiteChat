package com.example.litechat.view.activities


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.NewDocumentCreate
import com.example.litechat.view.adapters.GroupInfoAdapter
import com.facebook.spectrum.*
import com.facebook.spectrum.image.EncodedImageFormat
import com.facebook.spectrum.logging.SpectrumLogcatLogger
import com.facebook.spectrum.options.TranscodeOptions
import com.facebook.spectrum.requirements.EncodeRequirement
import com.facebook.spectrum.requirements.ResizeRequirement
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_group_info.*
import java.io.ByteArrayOutputStream
import java.lang.Exception


class GroupInfoActivity : AppCompatActivity() {


    private var nmemlist = ArrayList<String>()
    private lateinit var viewManager: RecyclerView.LayoutManager
    val REQUEST = 1

    var id: String = " "
    var url = " "
    var groupNameFromIntent = " "
    var data = FirebaseFirestore.getInstance()
    var reference: StorageReference? = null

    var nme: String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.example.litechat.R.layout.activity_group_info)
        id = intent.getStringExtra("documentPathId")
        groupNameFromIntent = intent.getStringExtra("groupName")
        url = intent.getStringExtra("url")

        if (url != " ")
            Glide.with(applicationContext).load(url).into(groupImage)
        else
            Glide.with(applicationContext).load(com.example.litechat.R.drawable.ic_group).into(groupImage)


        Log.d("ImageGroupImageUpadte", "image dowwnloaded to be updated in device GroupName :$groupNameFromIntent")

        groupImageButtonChange.setOnClickListener {

            val groupimg: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(groupimg, REQUEST)
        }

        viewManager = LinearLayoutManager(this)
        data.collection("Chats").document(id).get().addOnSuccessListener { result ->
            Log.d("Data", "Query" + result.toString())
            var obj = result.toObject(NewDocumentCreate::class.java)
            nmemlist = obj!!.groupmembers
            groupName.text = obj!!.groupname
            nme = obj!!.groupname
            Log.d("names", nmemlist.toString())
            var viewAdapter = GroupInfoAdapter(nmemlist, this)
            recyclerViewGroupinfo.apply {
                Log.d("names", "executed")
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
        leaveGroup.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var documentReference=data.collection("Chats").document(id)
                var itemRef=data.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
                documentReference.update("groupmembers",FieldValue.arrayRemove(AllChatDataModel.userNumberIdPM)).addOnSuccessListener {

                    Toast.makeText(this@GroupInfoActivity,"Left group..Now you can't send messages to this group",Toast.LENGTH_SHORT).show()

                    data.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
                documentReference.update("groupmembers", FieldValue.arrayRemove(AllChatDataModel.userNumberIdPM))
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@GroupInfoActivity,
                            "Left group..Now you can't send messages to this group",
                            Toast.LENGTH_SHORT
                        ).show()
                        data.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
                            .whereEqualTo("otherNumber", nme).get().addOnCompleteListener {
                                for (document in it.getResult()!!) {
                                    itemRef.document(document.getId()).delete()
                                }
                            }

                        }
                     var intent=Intent(this@GroupInfoActivity,HomeActivity::class.java)
                    startActivity(intent)
                    documentReference.update("usernumber",FieldValue.arrayRemove(AllChatDataModel.userNumberIdPM)).addOnSuccessListener {
                       //hard-coded as first element of array list needs to be changed...
                        documentReference.update("usernumber",FieldValue.arrayUnion(nmemlist[0]))
                    }
                }


            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        ProgressBarGroup.isIndeterminate
        ProgressBarGroup.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )


        if (data != null) {
            val fullPhotoUri: Uri? = data.data
            Log.d("ImageGroupUpload", fullPhotoUri.toString() + "resolv type ${data.resolveType(contentResolver)}")
            if (requestCode == 1 && resultCode == Activity.RESULT_OK && data.resolveType(contentResolver).toString().contains(
                    "image"
                )
            ) {


                val storageRef = FirebaseStorage.getInstance().reference
                //val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri)
                val baos = ByteArrayOutputStream()
                // bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos)
                val inputStream = contentResolver.openInputStream(fullPhotoUri)
                SpectrumSoLoader.init(this)
                val mSpectrum = Spectrum.make(
                    SpectrumLogcatLogger(Log.INFO),
                    DefaultPlugins.get()
                )
                mSpectrum!!.transcode(
                    EncodedImageSource.from(inputStream), EncodedImageSink.from(baos),
                    TranscodeOptions.Builder(
                        EncodeRequirement
                            (EncodedImageFormat.JPEG, EncodeRequirement.Mode.LOSSY)
                    ).resize(ResizeRequirement.Mode.EXACT_OR_SMALLER, 720)
                        .build(), applicationContext
                )


                val data1 = baos.toByteArray()
                Log.d("ImageGroupInfoActivity18", "Upload begins")
                FirebaseStorage.getInstance().getReference().child("groupimages").child(id).delete()
                    .addOnSuccessListener {

                        val uploadTask = storageRef.child("groupimages").child(id).putBytes(data1)
                        uploadTask.addOnFailureListener {
                            // Handle unsuccessful uploads
                            Log.d("ImageGroupInfoActivity19", "Upload taslk on failure ${it}")
                        }.addOnSuccessListener {
                            Log.d(
                                "ImageGroupInfoActivity20",
                                "Image uploaded sucessfully Group compressed id :$id and URL:$url"
                            )
                            //groupImage.setImageURI(fullPhotoUri)

                            FirebaseStorage.getInstance().getReference().child("groupimages").child(id)
                                .downloadUrl.addOnSuccessListener {
                                Log.d("FinalImage", it.toString())

                                Glide.with(applicationContext).load(it.toString())
                                    .apply(RequestOptions().placeholder(R.drawable.group_icon_compressed))
                                    .into(groupImage)
                                try {
                                    AllChatDataModel.urlList.find { it.chatDocumentId == id }!!.URL = it.toString()
                                } catch (e: Exception) {

                                }
                                ProgressBarGroup.visibility = View.INVISIBLE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            }

                        }

                    }

            } else {
                Toast.makeText(applicationContext, "Please select a valid image", Toast.LENGTH_LONG).show()
            }
        } else {
            ProgressBarGroup.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            Toast.makeText(applicationContext, "No Image Selected", Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}