package com.example.litechat.contracts

import com.example.litechat.model.ChatModelK

class AllChatsContractFrag {
    interface CFView{
           fun setContactstoFragmentChat()
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
        fun personalChatsDataRecieved()
        fun groupChatsDataRecieved()

        // methods to get chats from interactor in presenter
    }

}