package com.ssikira.things.data

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

sealed class Filter {
    data object Inbox : Filter()
    data object Logbook : Filter()
    data class Project(val projectId: Int) : Filter()
    data class Today(val today: Date = getTodayDate()) : Filter()
}

fun getTodayDate(): Date {
    val localDate = LocalDate.now()
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}