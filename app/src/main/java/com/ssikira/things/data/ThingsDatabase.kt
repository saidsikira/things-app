package com.ssikira.things.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Item::class], version = 1)
@TypeConverters(Converters::class)
abstract class ThingsDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemDao

    companion object : SingletonHolder<ThingsDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            ThingsDatabase::class.java, "things.db"
        ).build()
    })
}

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}