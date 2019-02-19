package com.example.litechat.contracts

import com.example.litechat.model.ChatModel

class AllChatsContractFrag {
    interface CFView{

    }
    interface CFInteractor{
        fun getPersonalChats(): ChatModel
        fun getGroupChats(): ChatModel
    }
    interface CFPresenter{
        fun setMessage()
        fun getChats()
        fun contactsToBeShown(arr:Array<Pair<String, String>>)

        // methods to get chats from interactor in presenter
    }

}