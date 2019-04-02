package com.example.litechat.listeners


class ListenerToPassString {

    interface Listener {
        fun onDataRecieved(string : String)


    }

    var listener: Listener? = null

    fun setCustomObjectListener(listener: Listener) {

        this.listener = listener
    }

}