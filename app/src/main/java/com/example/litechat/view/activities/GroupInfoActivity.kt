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
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.litechat.R
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.NewDocumentCreate
import com.example.litechat.view.adapters.GroupInfoAdapter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_group_info.*
import com.google.firebase.firestore.DocumentSnapshot




class GroupInfoActivity : AppCompatActivity() {


    private  var nmemlist= ArrayList<String>()
    private lateinit var viewManager: RecyclerView.LayoutManager
    val REQUEST = 1
    var id:String=" "
    var data=FirebaseFirestore.getInstance()
    var reference:StorageReference?=null
    var groupPic:Uri?=null
    var nme:String=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.litechat.R.layout.activity_group_info)
        id = intent.getStringExtra("documentPathId")
        Glide.with(applicationContext).load(com.example.litechat.R.drawable.ic_group).into(groupImage)
        FirebaseStorage.getInstance().reference.child("groupimages").child(id).downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext).load(it).apply(RequestOptions().placeholder(this.getDrawable(com.example.litechat.R.drawable.ic_group)))
                .into(groupImage)
        }
        groupImageButtonChange.setOnClickListener{

            val groupimg:Intent= Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(groupimg, REQUEST)
        }

        viewManager = LinearLayoutManager(this)
        data.collection("Chats").document(id).get().addOnSuccessListener { result->
          Log.d("Data","Query"+result.toString() )
           var obj= result.toObject(NewDocumentCreate::class.java)
            nmemlist=obj!!.groupmembers
            groupName.text = obj.groupname
            nme=obj.groupname
            Log.d("names",nmemlist.toString())
            var viewAdapter = GroupInfoAdapter(nmemlist,this)
            recyclerViewGroupinfo.apply {
                Log.d("names","executed")
                layoutManager=viewManager
                adapter=viewAdapter
            }
        }
        leaveGroup.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var documentReference=data.collection("Chats").document(id)
                var itemRef=data.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
                documentReference.update("groupmembers",FieldValue.arrayRemove(AllChatDataModel.userNumberIdPM)).addOnSuccessListener {
                    Toast.makeText(this@GroupInfoActivity,"Left group..Now you can't send messages to this group",Toast.LENGTH_SHORT).show()
                    data.collection("Users").document(AllChatDataModel.userNumberIdPM).collection("currentChats")
                       .whereEqualTo("otherNumber",nme).get().addOnCompleteListener {
                            for (document in it.getResult()!!) {
                                itemRef.document(document.getId()).delete()
                            }
                        }
                }

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, groupData: Intent?) {
        ProgressBarGroup.isIndeterminate
        ProgressBarGroup.visibility=View.VISIBLE
        if(requestCode==1&&resultCode==Activity.RESULT_OK&&groupData!!.data.toString().contains("image")){
            groupPic =groupData!!.data
            reference=FirebaseStorage.getInstance().reference
            reference!!.child("groupimages").child(id).putFile(groupPic!!).addOnSuccessListener {
                   groupImage.setImageURI(groupPic)
                   ProgressBarGroup.visibility=View.INVISIBLE
                }
        }
        else{
            Toast.makeText(this,"Please select a valid image",Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, groupData)
    }
}

