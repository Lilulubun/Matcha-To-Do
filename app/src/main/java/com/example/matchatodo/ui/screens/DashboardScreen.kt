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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.matchatodo.data.Task

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
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = White)
                Spacer(Modifier.width(8.dp))
                Text("Create New Goal", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = BgCream
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // --- HEADER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Matcha To Do",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = TextEspresso
                    )
                    Text("Track your journey! 🍵", color = TextMuted)
                }
                MatchaMascotIcon(Modifier.size(48.dp))
            }

            // --- STATS ---
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(
                    title = "COMPLETED",
                    count = completedCount,
                    color = MatchaGreen,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Star
                )
                StatBox(
                    title = "ACTIVE",
                    count = activeCount,
                    color = Terracotta,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.List
                )
            }

            Spacer(Modifier.height(20.dp))

            // --- GRID ---
            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No goals yet.", color = TextMuted)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(goals) { goal ->
                        GoalCard(
                            goal = goal,
                            onClick = {
                                if (goal.isCompleted)
                                    onNavigateToReward(goal.id)
                                else
                                    onNavigateToActive(goal.id)
                            },
                            onDelete = { onDelete(goal.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier,
    icon: ImageVector
) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
            .padding(16.dp)
            .height(96.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = White, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                title,
                color = White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            count.toString(),
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val done = goal.tasks.count { it.isCompleted }
    val total = goal.tasks.size
    val progress = if (total > 0) done.toFloat() / total else 0f

    Box(
        modifier = Modifier
            .height(180.dp)
            .background(White, RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = TextEspresso.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {

            // --- PROGRESS BAR ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        color = MatchaGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(6.dp)
                        .background(
                            color = MatchaGreen,
                            shape = RoundedCornerShape(50)
                        )
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = goal.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextEspresso,
                maxLines = 2
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "$done/$total tasks",
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(Modifier.weight(1f))

            // --- DELETE (SUBTLE) ---
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.End)
                    .clickable { onDelete() }
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
fun DashboardScreenPreview() {
    val mockGoals = listOf(
        Goal(
            id = "1",
            name = "Read 5 books",
            tasks = listOf(
                Task(id = "t1", text = "Book 1", isCompleted = true),
                Task(id = "t2", text = "Book 2", isCompleted = true)
            ),
            isCompleted = true
        ),
        Goal(
            id = "2",
            name = "Workout 3x/week",
            tasks = listOf(
                Task(id = "t1", text = "Leg day", isCompleted = true),
                Task(id = "t2", text = "Chest day", isCompleted = false),
                Task(id = "t3", text = "Back day", isCompleted = false)
            ),
            isCompleted = false
        )
    )

    DashboardScreen(
        goals = mockGoals,
        onNavigateToCreate = {},
        onNavigateToActive = {},
        onNavigateToReward = {},
        onDelete = {}
    )
}

