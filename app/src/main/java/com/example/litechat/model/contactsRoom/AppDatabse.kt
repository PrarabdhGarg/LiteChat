package com.example.litechat.model.contactsRoom

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [User::class, URLInfo::class], version = 2)
abstract class AppDatabse: RoomDatabase(){

    abstract fun userDao(): UserDao

    abstract fun urlInfoDao(): URLInfoDao
}