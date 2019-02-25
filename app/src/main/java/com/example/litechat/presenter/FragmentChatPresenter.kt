package com.example.litechat.presenter

import android.util.Log
import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.interactors.FragmentChatInteractor
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.MessageList

class FragmentChatPresenter(var view: AllChatsContractFrag.CFView):AllChatsContractFrag.CFPresenter {



     var fragmentChatInteractor=FragmentChatInteractor(this)

    override fun setMessage() {

    }

    // methods to set data in fragmentChat
    override fun personalChatsDataRecievedN1(allChatArrayListN1 :ArrayList<MessageList>) {
             /* view.setPersonalChatN1(allChatArrayListN1)*/
    }

    override fun personalChatsDataRecievedN2(allChatArrayListN2: ArrayList<MessageList>) {
/*
              view.setPersonalChatN2(allChatArrayListN2)
*/
    }

    override fun groupChatsDataRecieved(groupChatNames:ArrayList<String>) {
                 view.setGroupNames(groupChatNames)
    }

    override fun personalChatsDataRecieved(currentPersonalChatNames: ArrayList<String>) {

        /*Log.d("Please",AllChatDataModel.allChatArrayListN1Static.size.toString())
        Log.d("Please1",allChatArrayListN1.size.toString())*/
        view.setPersonalChatNames(currentPersonalChatNames)
    }

    override fun getChats() {
        fragmentChatInteractor.getPersonalChats()
       fragmentChatInteractor.getGroupChats()
    }
}