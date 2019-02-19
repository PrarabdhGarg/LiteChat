package com.example.litechat.presenter

import com.example.litechat.contracts.ChatContract
import com.example.litechat.interactors.ChatInteractor

class ChatPresenter : ChatContract.CPresenter {
     var chatInteractor =  ChatInteractor()
    override fun getMessageFromId(number: String) {
        chatInteractor.getMessagefromId(number)
    }

    override fun setMessage() {
         }


}