package com.example.litechat

class ListenerObjectTry {

    interface Listener {
        fun onDataRecieved(number : String)

    }

    var listener: Listener? = null

    fun setCustomObjectListener(listener: Listener) {

        this.listener = listener
    }


}