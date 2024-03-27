package com.ssikira.things.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date

@Dao
interface ItemDao {
    @Insert
    fun insertItem(vararg item: Item)

    @Query("SELECT * from item WHERE date_completed IS NULL ORDER BY date_created DESC")
    fun getAll(): Flow<List<Item>>

    @Query("UPDATE item set date_completed = :dateCompleted WHERE id = :itemId")
    fun completeItem(itemId: Int, dateCompleted: Date)

    @Update
    fun updateItem(item: Item)
}

fun ItemDao.markCompleted(item: Item) {
    val currentDate = Calendar.getInstance().time
    this.completeItem(itemId = item.id, dateCompleted = currentDate)
}