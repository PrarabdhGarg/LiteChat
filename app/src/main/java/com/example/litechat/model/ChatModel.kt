package com.example.litechat.model

data class ChatModel(
    val currentChats:Array<Pair<String, String>>,
    val Chats:Array<Pair<String, MutableMap<String, Any>>>
)

