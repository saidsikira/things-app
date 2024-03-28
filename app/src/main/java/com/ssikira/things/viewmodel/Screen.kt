package com.ssikira.things.viewmodel

sealed class Screen(val route: String, val title: String) {
    data object Inbox : Screen(route = "inbox", title = "Inbox")
    data object Today : Screen(route = "today", title = "Today")
    data object Logbook : Screen(route = "log", title = "Logbook")
    class Project(val id: Int, private val name: String) :
        Screen(route = "project/$id", title = name)

    companion object {
        fun forRoute(route: String): Screen {
            return when (route) {
                Inbox.route -> Inbox
                Today.route -> Today
                Logbook.route -> Logbook
                else -> return Inbox
            }
        }
    }
}