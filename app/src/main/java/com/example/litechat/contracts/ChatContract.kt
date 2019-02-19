package com.example.litechat.contracts

interface ChatContract {
    interface CView{
      fun displayMessage()
    }
  interface CInteractor{
   /*     fun getPersonalChats():  Array<Pair<String,MutableMap<String,Any>>>
        fun getGroupChats():  Array<Pair<String,MutableMap<String,Any>>>*/
    }
    interface CPresenter{
      /*fun setMessage()
      fun getMessageFromId()
        // methods to get chats from interactor in presenter*/
    }
}