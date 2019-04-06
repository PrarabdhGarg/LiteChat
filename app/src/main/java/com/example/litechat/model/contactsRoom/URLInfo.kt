package com.example.litechat.model.contactsRoom

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull



@Entity(tableName = "URLCollection")
data class URLInfo(

    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "chatDocumentId")
    var chatDocumentId: String,

    @NotNull
    @ColumnInfo(name = "URL")
    var URL: String
)