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

    @Query("SELECT * FROM item WHERE project_id = :projectId")
    fun getItemsByProject(projectId: Int): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE due_date = :today")
    fun getItemsByDate(today: Date): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE project_id IS NULL AND due_date IS NULL AND date_completed IS NULL")
    fun getItemsInInbox(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE date_completed IS NOT NULL")
    fun getItemsInLogbook(): Flow<List<Item>>
}

fun ItemDao.markCompleted(item: Item) {
    val currentDate = Calendar.getInstance().time
    this.completeItem(itemId = item.id, dateCompleted = currentDate)
}