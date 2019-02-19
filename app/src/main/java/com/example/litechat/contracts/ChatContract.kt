package com.example.litechat.contracts

interface ChatContract {
    interface CView{

      fun displayMessage()
    }
    interface CInteractor{
        fun getMessage(number:String)
        fun getAllCurrentChatNames(number:String)
    }
    interface CPresenter{
      fun setMessage()
      fun getMessageFromId(number: String)

    }
}