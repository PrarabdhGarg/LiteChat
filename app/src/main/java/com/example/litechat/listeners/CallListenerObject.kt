package com.example.litechat.listeners

class CallListenerObject{

    interface CallListener{

        fun startCallIntent(number: String)
    }

    var callListener: CallListener? = null

    fun setListener(passedListener: CallListener){

        this.callListener = passedListener
    }
}