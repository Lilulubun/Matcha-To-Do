package com.example.matchatodo.ui.screens

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.ui.components.StepScaffold
import com.example.matchatodo.ui.theme.*

@Composable
fun ReviewGoalScreen(
    goalName: String,
    tasks: List<String>,
    onBack: () -> Unit,
    onStartGoal: () -> Unit
) {
    StepScaffold(
        title = "Ready to Start?",
        stepText = "Step 3: Review your goal",
        activeStep = 2,
        ctaText = "Start My Goal! 🚀",
        ctaEnabled = true,
        onBack = onBack,
        onCtaClick = onStartGoal
    ) {

        // --- GOAL CARD ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(16.dp))
                .border(2.dp, TextEspresso, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {

            Text(
                text = "YOUR GOAL",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = goalName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextEspresso
            )

            Spacer(Modifier.height(12.dp))

            Divider(
                color = TextMuted.copy(alpha = 0.4f),
                thickness = 1.dp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "SUB-TASKS (${tasks.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )

            Spacer(Modifier.height(12.dp))

            // --- TASK LIST (READ-ONLY) ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tasks.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BgCream, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(MatchaGreen, RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = tasks[index],
                            fontSize = 14.sp,
                            color = TextEspresso
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // --- MOTIVATION CARD ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MatchaGreen, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "🎯 You’re about to start an amazing journey!\n" +
                        "Break your goal into small steps and celebrate each win!",
                color = White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewGoalScreenPreview() {
    ReviewGoalScreen(
        goalName = "Workout 3x/week",
        tasks = listOf(
            "Back",
            "Leg",
            "Chess"
        ),
        onBack = {},
        onStartGoal = {}
    )
}
