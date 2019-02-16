package com.example.litechat.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litechat.R
import com.example.litechat.view.adapters.DeveloperAdapter
import kotlinx.android.synthetic.main.fragment_developer.view.*

class FragmentDevelopers: Fragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_developer, container, false)

        val nameAndImages = arrayListOf(Pair("Suyash Soni",R.drawable.suyash),Pair("Prarabdh Garg",R.drawable.prarabdh),
            Pair("Akshat Gupta",R.drawable.akshat),Pair("Ishita Aggrawal",R.drawable.ishita))

        val fnameAndImages = nameAndImages.shuffled()

        viewManager = LinearLayoutManager(activity)
        viewAdapter = DeveloperAdapter(fnameAndImages, context!!)
        view.developerRecycler.apply {

            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }
}