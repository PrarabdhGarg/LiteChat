package com.example.litechat.listeners

class ListenerForFragmentChat {

    interface Listener {
        fun onDataRecieved(number : String, chatDocumentId: String, lastUpdated:String, urlToPass :String)


    }

    var listener: Listener? = null

    fun setCustomObjectListener(listener: Listener) {

        this.listener = listener
    }

}