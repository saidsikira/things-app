package com.ssikira.things.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.capitalize
import com.ssikira.things.viewmodel.Screen
import kotlinx.coroutines.launch

@Composable
fun ThingsBottomBar(onAdd: () -> Unit) {
    BottomAppBar(
        actions = {},
        floatingActionButton = {
            FloatingActionButton(
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                onClick = onAdd
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsTopBar(
    screen: Screen,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onDrawerAction: () -> Unit
) {
    LargeTopAppBar(
        title = { Text(text = screen.title) },
        navigationIcon = {
            IconButton(onClick = onDrawerAction) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        scrollBehavior = scrollBehavior
    )
}