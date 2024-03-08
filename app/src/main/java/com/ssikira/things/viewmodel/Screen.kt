package com.ssikira.things.viewmodel

sealed class Screen(val route: String, val title: String) {
    data object Inbox : Screen(route = "inbox", title = "Inbox")
    data object Today : Screen(route = "today", title = "Today")
}