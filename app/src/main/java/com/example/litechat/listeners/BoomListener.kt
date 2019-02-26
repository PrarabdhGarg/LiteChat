package com.example.litechat.listeners

class BoomListener{

    interface Boom{

        fun doThis()
    }

    var listener: Boom? = null

    fun setCustomObjectListener(bmbListener: Boom){

        this.listener = bmbListener

    }
}