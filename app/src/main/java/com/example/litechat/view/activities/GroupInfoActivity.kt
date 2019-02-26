package com.example.litechat.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewManager
import com.example.litechat.R
import com.example.litechat.model.NewDocumentCreate
import com.example.litechat.view.adapters.GroupInfoAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_group_info.*

class GroupInfoActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var nmemlist:ArrayList<String>
    var data=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)
        var id=intent.getStringExtra("documentPathId")
        data!!.collection("Chats").document(id).get().addOnSuccessListener { result->

           var obj= result.toObject(NewDocumentCreate::class.java)
            nmemlist=obj!!.groupmembers
            groupName.setText(obj!!.groupname)
        }
        viewManager=LinearLayoutManager(applicationContext)
        adapter=GroupInfoAdapter(nmemlist);
    }
}
