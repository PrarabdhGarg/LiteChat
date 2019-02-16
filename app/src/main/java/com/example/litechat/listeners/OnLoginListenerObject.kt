package com.example.litechat.listeners

class OnLoginListenerObject{

    var listener: OnLoginListener? = null

    fun setCustomObjectListener(listener: OnLoginListener) {

        this.listener = listener
    }

}