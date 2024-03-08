package com.ssikira.things.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsSearchBar(
    modifier: Modifier = Modifier,
    onDrawerAction: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val animatedPadding by animateDpAsState(
        animationSpec = spring(),
        targetValue = if (active) {
            0.dp
        } else {
            20.dp
        },
        label = "padding"
    )

    SearchBar(
        query = query,
        placeholder = { Text(text = "Search in your todos") },
        onQueryChange = { query = it },
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = animatedPadding),
        leadingIcon = {
            if (active) {
                IconButton(onClick = {
                    active = false
                    query = ""
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = { onDrawerAction() }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Search",
                    )
                }
            }
        },
        trailingIcon = {
            if (active && query != "") {
                IconButton(onClick = { query = "" }) {
                    Icon(Icons.Filled.Clear, contentDescription = "clear")
                }
            }
        }
    ) {
        LazyColumn {
            items(5) {
                Text("Hello")
            }
        }
    }
}