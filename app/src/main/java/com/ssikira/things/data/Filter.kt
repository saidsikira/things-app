package com.ssikira.things.data

import java.util.Date

sealed class Filter {
    data object Inbox : Filter()
    data class Project(val projectId: Int) : Filter()
    data class Today(val today: Date) : Filter()
}