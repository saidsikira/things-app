package com.ssikira.things.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class ThingsRepository(private val db: ThingsDatabase) {
    fun getAllItems(): Flow<List<Item>> {
        return db.itemsDao().getAll()
    }

    fun getItemsByProject(projectId: Int): Flow<List<Item>> {
        return db.itemsDao().getItemsByProject(projectId)
    }

    fun getItemsByDate(today: Date): Flow<List<Item>> {
        return db.itemsDao().getItemsByDate(today)
    }

    fun getItemsInInbox(): Flow<List<Item>> {
        return db.itemsDao().getItemsInInbox()
    }

    fun getItemsInLogbook(): Flow<List<Item>> {
        return db.itemsDao().getItemsInLogbook()
    }

    fun getAllProjects(): Flow<List<Project>> {
        return db.projectsDao().getAllProjects()
    }

    suspend fun insertItem(item: Item) {
        withContext(Dispatchers.IO) {
            db.itemsDao().insertItem(item)
        }
    }

    suspend fun insertProject(project: Project) {
        withContext(Dispatchers.IO) {
            db.projectsDao().insertProject(project)
        }
    }

    suspend fun markCompleted(item: Item) {
        withContext(Dispatchers.IO) {
            db.itemsDao().markCompleted(item)
        }
    }
}