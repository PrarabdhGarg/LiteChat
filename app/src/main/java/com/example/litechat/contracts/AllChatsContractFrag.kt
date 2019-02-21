package com.example.litechat.contracts

import com.example.litechat.model.ChatModelK
import com.example.litechat.model.MessageList

class AllChatsContractFrag {
    interface CFView{
           fun setContactstoFragmentChat()
           fun setGroupNames(groupChatNames:ArrayList<String>)
           fun setPersonalChatN1()
           fun setPersonalChatN2()
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
        fun contactsToBeShown(arr:Array<Pair<String, String>>)
        /**methods to notify presenter that data has been recieved
         * */
        fun personalChatsDataRecievedN1(allChatArrayListN1 :ArrayList<MessageList>)
        fun personalChatsDataRecievedN2(allChatArrayListN2 :ArrayList<MessageList>)
        fun groupChatsDataRecieved(groupChatNames:ArrayList<String>)

        // methods to get chats from interactor in presenter
    }

}