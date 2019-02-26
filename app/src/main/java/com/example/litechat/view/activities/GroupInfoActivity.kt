package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewManager
import com.example.litechat.R
import com.example.litechat.view.adapters.GroupInfoAdapter

class GroupInfoActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var nmemlist:List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)
        viewManager=LinearLayoutManager(applicationContext)
        adapter=GroupInfoAdapter(nmemlist);
    }
}
