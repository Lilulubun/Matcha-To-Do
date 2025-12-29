package com.example.matchatodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.ui.components.StepScaffold
import com.example.matchatodo.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalCreationScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Unit
) {
    var goalName by remember { mutableStateOf("") }

    val suggestions = listOf(
        "Read 5 books",
        "Workout 3x/week",
        "Learn coding",
        "Start a business"
    )

    StepScaffold(
        title = "Create New Goal",
        stepText = "Step 1: Name your goal",
        activeStep = 0,
        ctaText = "Next Step →",
        ctaEnabled = goalName.isNotBlank(),
        onBack = onBack,
        onCtaClick = { onContinue(goalName) }
    ) {

        Text(
            text = "WHAT DO YOU WANT TO ACHIEVE?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
        )

        Spacer(Modifier.height(8.dp))

        BasicTextField(
            value = goalName,
            onValueChange = { goalName = it },
            textStyle = TextStyle(fontSize = 18.sp, color = TextEspresso),
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(14.dp))
                .border(2.dp, MatchaGreen, RoundedCornerShape(14.dp))
                .padding(16.dp),
            decorationBox = { inner ->
                if (goalName.isEmpty()) {
                    Text(
                        text = "e.g. Learn Japanese cooking",
                        color = TextMuted.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                }
                inner()
            }
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "SUGGESTIONS:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { suggestion ->
                AssistChip(
                    onClick = { goalName = suggestion },
                    label = { Text(suggestion) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = White,
                        labelColor = TextEspresso
                    ),
                    modifier = Modifier.border(
                        1.dp,
                        MatchaGreen,
                        RoundedCornerShape(50)
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GoalCreationScreenPreview() {
    GoalCreationScreen(
        onBack = {},
        onContinue = {}
    )
}
