package com.example.litechat.model

import android.util.Pair

data class ChatModelK(

    var currentChats: Pair<String, String> ,
    var chats:kotlin.Pair<String, MutableMap<String, kotlin.Any>>
)