package com.example.litechat.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewManager
import com.example.litechat.R
import com.example.litechat.model.NewDocumentCreate
import com.example.litechat.view.adapters.GroupInfoAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_group_info.*

class GroupInfoActivity : AppCompatActivity() {
     private var nmemlist=ArrayList<String>()
    //private  var nmemlist= List<String>
    var data=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)
        var id=intent.getStringExtra("documentPathId")
        Log.d("Data","Query Called$id")
        var adapter = GroupInfoAdapter(nmemlist)
        recyclerViewGroupinfo.adapter=adapter
        nmemlist.clear()
        data.collection("Chats").document(id).get().addOnSuccessListener { result->

          Log.d("Data","Query"+result.toString() )
           var obj= result.toObject(NewDocumentCreate::class.java)
            nmemlist.clear()
            nmemlist.addAll(obj!!.groupmembers)
            adapter.notifyDataSetChanged()
            Log.d("DataRetrived", "Mebers$nmemlist")
            groupName.setText(obj.groupname)
        }


    }
}

