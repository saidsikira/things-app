package com.ssikira.things.viewmodel

import com.ssikira.things.data.Filter

sealed class Screen(val route: String, val title: String) {
    data object Inbox : Screen(route = "inbox", title = "Inbox")
    data object Today : Screen(route = "today", title = "Today")
    data object Logbook : Screen(route = "log", title = "Logbook")
    class Project(val id: Int, private val name: String) :
        Screen(route = "project/$id", title = name)
}

fun Screen.toFilter(): Filter {
    return when (this) {
        is Screen.Inbox -> Filter.Inbox
        is Screen.Today -> Filter.Today()
        is Screen.Logbook -> Filter.Logbook
        is Screen.Project -> Filter.Project(this.id)
    }
}

fun Screen.projectId(): Int? {
    return when (this) {
        is Screen.Project -> this.id
        else -> return null
    }
}
