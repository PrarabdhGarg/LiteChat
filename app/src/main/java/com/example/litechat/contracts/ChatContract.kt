package com.example.litechat.contracts

interface ChatContract {
    interface CView{
      fun displayMessage()
    }
    interface CInteractor{
        fun getMessage()
    }
    interface CPresenter{
      fun setMessage()


    }
}