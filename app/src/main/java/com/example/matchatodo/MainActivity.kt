package com.example.matchatodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.matchatodo.ui.theme.MatchaGreen
//commit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Start the Navigation Hub
            MatchaApp()
        }
    }
}