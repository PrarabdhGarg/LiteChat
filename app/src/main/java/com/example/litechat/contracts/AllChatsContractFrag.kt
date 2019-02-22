package com.example.litechat.contracts

import com.example.litechat.model.ChatModelK
import com.example.litechat.model.MessageList

class AllChatsContractFrag {
    interface CFView{
           fun setContactstoFragmentChat()
           fun setGroupNames(groupChatNames:ArrayList<String>)
           fun setPersonalChatNames(personalChatNames:ArrayList<String>,personalChats:ArrayList<MessageList>)
           fun setPersonalChatN1(personalChatNamesN1:ArrayList<MessageList>)
           fun setPersonalChatN2(personalChatNamesN2:ArrayList<MessageList>)
    }
    interface CFInteractor{

        fun getPersonalChats()
        fun getGroupChats()
        /*fun getPersonalChats(): ArrayList<ChatModelK>
        fun getGroupChats(): ArrayList<ChatModelK>*/
    }
    interface CFPresenter{
        fun setMessage()
        fun getChats()

        /**methods to notify presenter that data has been recieved
         * */
        fun personalChatsDataRecievedN1(allChatArrayListN1 :ArrayList<MessageList>)
        fun personalChatsDataRecievedN2(allChatArrayListN2 :ArrayList<MessageList>)
        fun personalChatsDataRecieved(currentPersonalChatNames: ArrayList<String>,allChatArrayListN1 :ArrayList<MessageList>)
        fun groupChatsDataRecieved(groupChatNames:ArrayList<String>)

        // methods to get chats from interactor in presenter
    }

}