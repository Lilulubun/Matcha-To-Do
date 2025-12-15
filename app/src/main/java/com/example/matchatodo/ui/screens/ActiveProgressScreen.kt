package com.example.matchatodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.data.Goal
import com.example.matchatodo.data.Task
import com.example.matchatodo.ui.theme.*

// 1. STATEFUL (Connects to Logic)
@Composable
fun ActiveProgressScreen(
    goal: Goal?,
    onBack: () -> Unit,
    onToggleTask: (String) -> Unit,
    onGoalCompleted: () -> Unit
) {
    if (goal == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Loading...") }
        return
    }

    LaunchedEffect(goal.isCompleted) {
        if (goal.isCompleted) onGoalCompleted()
    }

    ActiveProgressContent(goal, onBack, onToggleTask)
}

// 2. STATELESS (UI Only - Good for Previews)
@Composable
fun ActiveProgressContent(
    goal: Goal,
    onBack: () -> Unit,
    onToggleTask: (String) -> Unit
) {
    val doneCount = goal.tasks.count { it.isCompleted }

    Column(Modifier.fillMaxSize().background(BgCream)) {
        Box(Modifier.padding(24.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextEspresso) }
        }

        Box(Modifier.fillMaxWidth().height(280.dp), contentAlignment = Alignment.Center) {
            MatchaCupCharacter(doneCount, goal.tasks.size, Modifier.size(200.dp, 280.dp))
        }

        Column(Modifier.fillMaxSize().background(White, RoundedCornerShape(topStart=32.dp, topEnd=32.dp)).padding(24.dp)) {
            Text(goal.name, fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextEspresso)
            Text("$doneCount/${goal.tasks.size} steps", color = TextMuted)
            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(goal.tasks) { task ->
                    Row(
                        Modifier.fillMaxWidth()
                            .background(if(task.isCompleted) MatchaGreen.copy(0.2f) else BgCream, RoundedCornerShape(12.dp))
                            .clickable { onToggleTask(task.id) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (task.isCompleted) {
                            Icon(Icons.Default.CheckCircle, null, tint = MatchaGreen, modifier = Modifier.size(24.dp))
                        } else {
                            Box(Modifier.size(24.dp).border(2.dp, TextMuted, CircleShape))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(task.text, textDecoration = if(task.isCompleted) TextDecoration.LineThrough else null)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActive() {
    ActiveProgressContent(
        Goal(name = "Preview Goal", tasks = listOf(Task(text="Task 1", isCompleted=true), Task(text="Task 2"))),
        {}, {}
    )
}