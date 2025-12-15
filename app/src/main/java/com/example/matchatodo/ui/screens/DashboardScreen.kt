package com.example.matchatodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List // Standard Icon (Fixes TrackChanges)
import androidx.compose.material.icons.filled.Star // Standard Icon (Fixes EmojiEvents)
import androidx.compose.material3.* // Fixes "Unresolved reference: Text/Card"
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.data.Goal
import com.example.matchatodo.ui.theme.*

@Composable
fun DashboardScreen(
    goals: List<Goal>,
    onNavigateToCreate: () -> Unit,
    onNavigateToActive: (String) -> Unit,
    onNavigateToReward: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val completedCount = goals.count { it.isCompleted }
    val activeCount = goals.size - completedCount

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = onNavigateToCreate,
                modifier = Modifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MatchaGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = White)
                Spacer(Modifier.width(8.dp))
                Text("Create New Goal", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = BgCream
    ) { p ->
        Column(Modifier.padding(p).fillMaxSize()) {
            // Header
            Row(Modifier.padding(24.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Matcha To Do", fontSize = 32.sp, fontWeight = FontWeight.Black, color = TextEspresso)
                    Text("Track your journey! 🍵", color = TextMuted)
                }
                MatchaMascotIcon(Modifier.size(50.dp))
            }

            // Stats
            Row(Modifier.padding(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Using Standard Icons here to prevent errors
                StatBox("COMPLETED", completedCount, MatchaGreen, Modifier.weight(1f), Icons.Default.Star)
                StatBox("ACTIVE", activeCount, Terracotta, Modifier.weight(1f), Icons.Default.List)
            }
            Spacer(Modifier.height(16.dp))

            // Grid
            if (goals.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No goals yet.", color = TextMuted)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(goals) { goal ->
                        GoalCard(goal,
                            onClick = { if (goal.isCompleted) onNavigateToReward(goal.id) else onNavigateToActive(goal.id) },
                            onDelete = { onDelete(goal.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(title: String, count: Int, color: Color, modifier: Modifier, icon: ImageVector) {
    Box(modifier.height(100.dp).background(color, RoundedCornerShape(16.dp)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = White, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(title, color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Text(count.toString(), color = White, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomStart))
    }
}

@Composable
fun GoalCard(goal: Goal, onClick: () -> Unit, onDelete: () -> Unit) {
    val done = goal.tasks.count { it.isCompleted }
    val progress = if(goal.tasks.isNotEmpty()) done.toFloat()/goal.tasks.size else 0f

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        border = androidx.compose.foundation.BorderStroke(2.dp, TextEspresso),
        modifier = Modifier.height(180.dp).clickable { onClick() }
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(80.dp).background(if(goal.isCompleted) MatchaGreen else Color.LightGray.copy(0.3f))) {
                Text("${(progress*100).toInt()}%", modifier = Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold, color = if(goal.isCompleted) White else TextEspresso)
                IconButton(onClick = onDelete, modifier = Modifier.align(Alignment.TopEnd)) {
                    Icon(Icons.Default.Delete, null, tint = TextEspresso)
                }
            }
            Column(Modifier.padding(12.dp)) {
                Text(goal.name, fontWeight = FontWeight.Bold, maxLines = 2)
                Text("$done/${goal.tasks.size} tasks", fontSize = 12.sp, color = TextMuted)
            }
        }
    }
}