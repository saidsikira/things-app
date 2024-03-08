package com.ssikira.things.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    fun insertItem(vararg item: Item)

    @Query("SELECT * from item WHERE date_completed IS NULL ORDER BY date_created DESC")
    fun getAll(): Flow<List<Item>>
}