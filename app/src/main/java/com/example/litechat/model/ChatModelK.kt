package com.example.litechat.model

import android.util.Pair

data class ChatModelK(

    var currentChats: Pair<String, ArrayList<MessageModel>> ,
    var chats:kotlin.Pair<String, MutableMap<String, kotlin.Any>>
)