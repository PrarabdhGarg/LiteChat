package com.example.litechat.model

data class UserDataModel(
    val number : String,
    val id : String,
    var about : String = "",
    var name : String ,
    var currentChats : HashMap<String , String> = HashMap()
)