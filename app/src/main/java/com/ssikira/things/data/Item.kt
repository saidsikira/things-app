package com.ssikira.things.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "details") val details: String?,
    @ColumnInfo(name = "date_created") val dateCreated: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "date_completed") val dateCompleted: Date? = null
)

fun Item.isCompleted(): Boolean {
    return dateCompleted != null
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
