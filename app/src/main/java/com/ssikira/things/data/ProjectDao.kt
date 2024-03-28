package com.ssikira.things.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM project WHERE id==:id LIMIT 1")
    fun getProject(id: Int): Flow<Project?>

    @Insert
    fun insertProject(vararg project: Project)

    @Query("SELECT * from item WHERE date_completed IS NULL AND due_date IS NULL ORDER BY date_created DESC")
    fun getInbox(): Flow<List<Item>>

    @Query("SELECT * from item WHERE date_completed <= CURRENT_DATE ORDER BY date_created DESC")
    fun getToday(): Flow<List<Item>>

    @Query("SELECT * from item WHERE date_completed IS NOT NULL ORDER BY date_completed DESC")
    fun getLogbook(): Flow<List<Item>>


}