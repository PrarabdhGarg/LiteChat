package com.example.litechat.presenter

import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.interactors.FragmentChatInteractor
import com.example.litechat.model.MessageList

class FragmentChatPresenter(var view: AllChatsContractFrag.CFView):AllChatsContractFrag.CFPresenter {
    override fun personalChatsDataRecievedN2(allChatArrayListN2: ArrayList<MessageList>) {

    }

    override fun contactsToBeShown(arr: Array<Pair<String, String>>){

    }
    /*lateinit var pm:Array<Pair<String,MutableMap<String,Any>>>
    lateinit var group:Array<Pair<String,MutableMap<String,Any>>>*/
     var fragmentChatInteractor=FragmentChatInteractor(this)

    override fun setMessage() {

    }

    // methods to set data in fragmentChat
    override fun personalChatsDataRecievedN1(allChatArrayListN1 :ArrayList<MessageList>) {
              view.setContactstoFragmentChat()
    }

    override fun groupChatsDataRecieved(groupChatNames:ArrayList<String>) {
                 view.setGroupNames(groupChatNames)
    }

    override fun getChats() {
        fragmentChatInteractor.getPersonalChats()
       fragmentChatInteractor.getGroupChats()
    }
}