package com.example.litechat.contracts

interface ChatContract {
    interface CView{

      fun displayMessage()
    }
    interface CInteractor{
        fun getPersonalChats(s: String)
       // fun getGroupChats():  Array<Pair<String,MutableMap<String,Any>>>
    }
    interface CPresenter{
      fun setMessage()
      fun getMessageFromId(number: String)
        // methods to get chats from interactor in presenter



    }
}