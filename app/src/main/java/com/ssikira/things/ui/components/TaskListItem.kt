package com.ssikira.things.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.ssikira.things.data.Item
import com.ssikira.things.data.isCompleted
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(FlowPreview::class)
@Composable
fun TaskListItem(
    item: Item,
    onCheckedChange: () -> Unit
) {
    var isChecked by remember { mutableStateOf(item.isCompleted()) }
    var launchEffectKey by remember { mutableIntStateOf(0) }

    Card(
        onClick = {},
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    if (it) launchEffectKey++
                },
            )
            Text(
                color = if (isChecked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                text = item.title,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
    }

    LaunchedEffect(launchEffectKey) {
        if (isChecked) {
            delay(2000) // Wait for 2 seconds
            // Check again if isChecked is still true
            if (isChecked) {
                onCheckedChange()
            }
        }
    }
}

@Preview(showBackground = true, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Composable
fun TaskListPreview() {
    Column {
        TaskListItem(item = Item(title = "Hello", details = null), onCheckedChange = {

        })
        TaskListItem(item = Item(title = "Hello", details = null, dateCompleted = Calendar.getInstance().time), onCheckedChange = {

        })
    }
}