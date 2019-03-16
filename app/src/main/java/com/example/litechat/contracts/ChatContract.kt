package com.example.litechat.contracts

import android.content.Context
import com.example.litechat.model.MessageModel

interface ChatContract {
    interface CView{

      fun displayMessage()
      fun getOtherMessagesFromPresenter()


    }

    interface CInteractor{
        fun saveNewMessageToFirestore(messageModel: MessageModel,context: Context)
        fun getNewMessageFromFirestore()
        fun removeListener()

    }
    interface CPresenter{

        fun getNewOtherMessagesFromInteractor()
        fun passNewMessageToPresenter()
        fun passNewSetMessageFromViewtoPresenter(messageModel: MessageModel,context: Context)
        fun notifyModelOfBackPressed()
    }
}