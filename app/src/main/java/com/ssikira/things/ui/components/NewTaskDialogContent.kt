package com.ssikira.things.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.ssikira.things.R
import com.ssikira.things.data.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskDialogContent(
    onTaskAdded: (Item) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column {
        TextField(
            textStyle = MaterialTheme.typography.titleLarge,
            placeholder = {
                Text(text = "New task", style = MaterialTheme.typography.titleLarge)
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = text, onValueChange = {
                text = it
            }
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        BottomAppBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            floatingActionButton = {
                Button(
                    enabled = text != "",
                    onClick = {
                        val item = Item(title = text, details = null, dateCompleted = null)
                        onTaskAdded(item)
                    }) {
                    Text(text = "Save")
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Cal")
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = rememberVectorPainter(
                            image = ImageVector.vectorResource(
                                id = R.drawable.project
                            )
                        ),
                        contentDescription = "Inventory"
                    )
                }

            },
        )
    }
}

@Preview(showBackground = true, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Composable
fun NewTaskDialogContentPreview() {
    NewTaskDialogContent(onTaskAdded = {})
}