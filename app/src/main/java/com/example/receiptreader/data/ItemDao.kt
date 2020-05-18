package com.example.receiptreader.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item : Item)

    @Query("SELECT * FROM items ORDER BY purchase_date DESC")
    fun getAllItems() : List<Item>
}