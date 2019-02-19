package com.example.litechat.contracts

class AllChatsContractFrag {
    interface CFView{

    }
    interface CFInteractor{
        fun getPersonalChats():  Array<Pair<String,MutableMap<String,Any>>>
        fun getGroupChats():  Array<Pair<String,MutableMap<String,Any>>>
    }
    interface CFPresenter{
        fun setMessage()
        fun getChats()

        // methods to get chats from interactor in presenter
    }

}