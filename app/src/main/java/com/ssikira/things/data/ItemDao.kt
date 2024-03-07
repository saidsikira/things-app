package com.ssikira.things.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    fun insertItem(vararg item: Item)

    @Query("SELECT * from item")
    fun getAll(): Flow<List<Item>>
}