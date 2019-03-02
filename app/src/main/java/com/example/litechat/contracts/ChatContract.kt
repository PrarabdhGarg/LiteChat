package com.example.litechat.contracts

import android.content.Context
import com.example.litechat.model.MessageModel

interface ChatContract {
    interface CView{

      fun displayMessage()
        // tgo test snapshoplistenr
        fun getOtherMessagesFromPresenter()


    }

    interface CInteractor{
        fun saveNewMessageToFirestore(messageModel: MessageModel,context: Context)
        fun getNewMessageFromFirestore()
        fun removeListener()
        // method returns personal chats to user
      //  fun getPersonalChats(): ArrayList<ChatModelK>
      // fun getGroupChats():  Array<Pair<String,MutableMap<String,Any>>>

    }
    interface CPresenter{
        fun setGroupMessage()
        fun getNewOtherMessagesFromInteractor()
        fun passNewMessageToPresenter()
        fun passNewSetMessageFromViewtoPresenter(messageModel: MessageModel,context: Context)
        fun getNewMessageForView(messageModel: MessageModel)
        fun notifyModelOfBackPressed()
    }
}