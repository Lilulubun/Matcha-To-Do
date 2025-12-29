package com.example.matchatodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.ui.components.StepScaffold
import com.example.matchatodo.ui.theme.*

@Composable
fun AddTaskScreen(
    goalName: String,
    onBack: () -> Unit,
    onStartBrewing: (String, List<String>) -> Unit
) {
    var tasks by remember { mutableStateOf(listOf("")) }

    StepScaffold(
        title = "Break It Down",
        stepText = "Step 2: Add your sub-tasks",
        activeStep = 1,
        ctaText = "Preview Goal →",
        ctaEnabled = tasks.any { it.isNotBlank() },
        onBack = onBack,
        onCtaClick = {
            onStartBrewing(goalName, tasks.filter { it.isNotBlank() })
        }
    ) {

        Column {

            // --- TASK LIST (scrollable) ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(tasks) { index, task ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Number badge
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

                        // Input + clear button container
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(White, RoundedCornerShape(12.dp))
                                .border(1.5.dp, TextEspresso, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BasicTextField(
                                value = task,
                                onValueChange = { new ->
                                    val list = tasks.toMutableList()
                                    list[index] = new
                                    tasks = list
                                },
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    color = TextEspresso
                                ),
                                modifier = Modifier.weight(1f),
                                decorationBox = { inner ->
                                    if (task.isEmpty()) {
                                        Text(
                                            text = "Task ${index + 1}",
                                            color = TextMuted.copy(alpha = 0.6f)
                                        )
                                    }
                                    inner()
                                }
                            )

                            // --- CLEAR BUTTON ---
                            if (task.isNotEmpty()) {
                                Spacer(Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            color = BgCream,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clickable {
                                            val list = tasks.toMutableList()
                                            list[index] = ""
                                            tasks = list
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "✕",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- ADD ANOTHER TASK (NOW VISIBLE) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        1.5.dp,
                        TextEspresso,
                        RoundedCornerShape(42.dp)
                    )
                    .clickable { tasks = tasks + "" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+  Add Another Task",
                    fontWeight = FontWeight.Medium
                )
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    AddTaskScreen(
        goalName = "Finish Portfolio",
        onBack = {},
        onStartBrewing = { _, _ -> }
    )
}
