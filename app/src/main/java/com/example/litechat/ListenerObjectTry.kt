package com.example.litechat

class ListenerObjectTry {

    interface Listener {
        fun onDataRecieved(number : String, chatDocumentId: String, lastUpdated:String)


    }

    var listener: Listener? = null

    fun setCustomObjectListener(listener: Listener) {

        this.listener = listener
    }

}