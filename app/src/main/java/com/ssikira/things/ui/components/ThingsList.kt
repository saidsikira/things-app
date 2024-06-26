package com.ssikira.things.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssikira.things.data.Filter
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.data.ThingsRepository
import com.ssikira.things.viewmodel.ThingsViewModel
import com.ssikira.things.viewmodel.ItemsViewModelFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThingsList(
    filter: Filter,
    vm: ThingsViewModel = viewModel(
        factory = ItemsViewModelFactory(
            ThingsRepository(ThingsDatabase.getInstance(LocalContext.current))
        )
    )
) {
    vm.setFilter(filter);
    val x by vm.items.collectAsState()

    val filterDescription = remember(filter) { getFilterDescription(filter, vm) }

    LazyColumn {
        stickyHeader {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
//                Icon(
//                    Icons.Filled.Star,
//                    contentDescription = "Today",
//                    tint = MaterialTheme.colorScheme.surfaceTint,
//                    modifier = Modifier
//                        .size(38.dp)
//                        .padding(end = 8.dp)
//                )
                Text(
                    text = filterDescription,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        items(x) {
//            Text(modifier = Modifier.padding(horizontal = 16.dp), text = it.title)
            TaskListItem(item = it, onCheckedChange = {
                vm.markCompleted(it)
            })
        }
    }
}

fun getFilterDescription(filter: Filter, vm: ThingsViewModel): String {
    return when (filter) {
        is Filter.Inbox -> "Inbox"
        is Filter.Today -> "Today"
        is Filter.Logbook -> "Logbook"
        is Filter.Project -> vm.getProjectName(filter.projectId)
    }
}