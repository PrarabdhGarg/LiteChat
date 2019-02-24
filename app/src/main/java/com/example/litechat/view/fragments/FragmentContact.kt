package com.example.litechat.view.fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litechat.R

class FragmentContact: Fragment(){

    override fun onDestroyView() {
        Log.d("ViewPager" , "onDestroyView of FragmentContacts called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("ViewPager" , "onDestroy of FragmentContacts called")
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ViewPager" , "onCreate of FragmentContacts called")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d("ViewPager" , "onCreateView of FragmentContacts called")
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        return view

    }
}