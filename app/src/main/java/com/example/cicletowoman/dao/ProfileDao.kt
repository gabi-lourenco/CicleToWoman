package com.example.cicletowoman.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cicletowoman.entities.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile WHERE uid = :uid")
    fun getByUid(uid: String): Profile

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: Profile)

    @Query("DELETE FROM profile WHERE uid = :uid")
    fun delete(uid: String)
}