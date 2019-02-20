package com.example.litechat.presenter

import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.interactors.FragmentChatInteractor

class FragmentChatPresenter(var view: AllChatsContractFrag.CFView):AllChatsContractFrag.CFPresenter {

    override fun contactsToBeShown(arr: Array<Pair<String, String>>){

    }
    /*lateinit var pm:Array<Pair<String,MutableMap<String,Any>>>
    lateinit var group:Array<Pair<String,MutableMap<String,Any>>>*/
     var fragmentChatInteractor=FragmentChatInteractor(this)

    override fun setMessage() {

    }

    // methods to set data in fragmentChat
    override fun personalChatsDataRecieved() {
              view.setContactstoFragmentChat()
    }

    override fun groupChatsDataRecieved() {

    }

    override fun getChats() {
        fragmentChatInteractor.getPersonalChats()
       fragmentChatInteractor.getGroupChats()
    }
}