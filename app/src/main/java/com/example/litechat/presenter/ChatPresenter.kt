package com.example.litechat.presenter

import android.content.Context
import android.util.Log
import com.example.litechat.contracts.ChatContract
import com.example.litechat.interactors.ChatInteractor
import com.example.litechat.model.MessageModel

class ChatPresenter(var view:ChatContract.CView) : ChatContract.CPresenter {
    override fun notifyModelOfBackPressed() {
        chatInteractor.removeListener()
    }

    private var chatInteractor= ChatInteractor(this)

    override fun passNewSetMessageFromViewtoPresenter(messageModel: MessageModel,context: Context) {
        chatInteractor.saveNewMessageToFirestore(messageModel,context)
    }


    override fun passNewMessageToPresenter() {
        Log.d("Run4","View.displayMessagecalled")

        view.displayMessage()

    }

    override fun getNewOtherMessagesFromInteractor() {
      chatInteractor.getNewMessageFromFirestore()
    }

}