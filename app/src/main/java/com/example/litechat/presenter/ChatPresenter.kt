package com.example.litechat.presenter

import android.view.View
import com.example.litechat.contracts.ChatContract
import com.example.litechat.interactors.ChatInteractor

class ChatPresenter(var view:ChatContract.CView) : ChatContract.CPresenter {
    var chatInteractor =  ChatInteractor()
    override fun setGroupMessage() {

    }
}