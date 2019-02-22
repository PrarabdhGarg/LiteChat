package com.example.litechat.contracts

import com.example.litechat.model.ChatModelK

interface ChatContract {
    interface CView{

     // fun displayMessage()
    }

    interface CInteractor{
        // method returns personal chats to user
      //  fun getPersonalChats(): ArrayList<ChatModelK>
      // fun getGroupChats():  Array<Pair<String,MutableMap<String,Any>>>
    }
    interface CPresenter{
     // fun setMessage()
     // fun getMessageFromId()





    }
}