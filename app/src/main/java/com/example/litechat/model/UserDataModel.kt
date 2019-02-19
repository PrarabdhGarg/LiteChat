package com.example.litechat.model

data class UserDataModel(
    val Number : String,
    val Id : String,
    var About : String = "",
    var Name : String ,
    var CurrentChats : HashMap<String , String> = HashMap(),
    var CurrentActivity : String = "",
    var Image : String = ""
)