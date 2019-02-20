package com.example.litechat.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.litechat.R
import com.example.litechat.view.adapters.DeveloperAdapter
import kotlinx.android.synthetic.main.activity_developer.*

class DeveloperActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)

        val nameAndImages = arrayListOf(Pair("Suyash Soni",R.drawable.suyash),Pair("Prarabdh Garg",R.drawable.prarabdh),
            Pair("Akshat Gupta",R.drawable.akshat),Pair("Ishita Aggarwal",R.drawable.ishita))

        val fnameAndImages = nameAndImages.shuffled()

        viewManager = LinearLayoutManager(applicationContext)
        viewAdapter = DeveloperAdapter(fnameAndImages, applicationContext)

        developerRecycler.apply {

            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
