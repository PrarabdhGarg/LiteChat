package com.example.litechat.model.contactsRoom

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.litechat.model.contactsRoom.URLInfo
import java.lang.reflect.Array

@Dao
interface URLInfoDao {

    @Query("SELECT * FROM URLCollection")
    fun getAllURLInfo(): List<URLInfo>

   /* @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>*/

    @Query("SELECT * FROM URLCollection WHERE chatDocumentId LIKE :id")
    fun findBychatDocumentId(id: String): URLInfo

    @Insert
    fun insertAllURLdata(vararg URLData: URLInfo)

    @Query("DELETE FROM URLCollection")
    fun deleteAllURLData()
}