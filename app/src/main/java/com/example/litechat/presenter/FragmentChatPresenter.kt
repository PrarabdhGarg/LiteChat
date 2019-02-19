package com.example.litechat.presenter

import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.interactors.FragmentChatInteractor

class FragmentChatPresenter(view : AllChatsContractFrag.CFView):AllChatsContractFrag.CFPresenter {
    override fun contactsToBeShown(arr: Array<Pair<String, String>>){

    }
    lateinit var pm:Array<Pair<String,MutableMap<String,Any>>>
    lateinit var group:Array<Pair<String,MutableMap<String,Any>>>
     var fragmentChatInteractor=FragmentChatInteractor(this)

    override fun setMessage() {

    }

    override fun getChats() {
       pm = fragmentChatInteractor.getPersonalChats()
       group = fragmentChatInteractor.getGroupChats()
    }
}