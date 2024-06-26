package com.ssikira.things

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.ssikira.things.ui.components.ThingsApp
import com.ssikira.things.ui.theme.ThingsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ThingsTheme {
                Surface {
                    ThingsApp()
                }
            }
        }
    }
}