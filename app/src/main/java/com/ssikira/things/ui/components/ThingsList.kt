package com.ssikira.things.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.viewmodel.ThingsViewModel
import com.ssikira.things.viewmodel.ItemsViewModelFactory

@Composable
fun ThingsList(
    vm: ThingsViewModel = viewModel(
        factory = ItemsViewModelFactory(
            ThingsDatabase.getInstance(
                LocalContext.current
            )
        )
    )
) {
    val x by vm.items.collectAsState()

    LazyColumn {
        items(x) {
            Text(text = it.title)
        }
    }
}