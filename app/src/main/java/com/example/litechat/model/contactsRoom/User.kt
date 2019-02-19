package com.example.litechat.model.contactsRoom

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Contacts")
data class User(

    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "Number")
    var mobileNumber: String,

    @NotNull
    @ColumnInfo(name = "Name")
    var name: String
)
