package com.example.matchatodo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.matchatodo.ui.theme.*

@Composable
fun StepScaffold(
    title: String,
    stepText: String,
    activeStep: Int,          // 0-based (0,1,2)
    totalSteps: Int = 3,
    ctaText: String,
    ctaEnabled: Boolean = true,
    onBack: () -> Unit,
    onCtaClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
    ) {

        // --- MAIN CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            // Back
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            Spacer(Modifier.height(12.dp))

            // Header
            Column(horizontalAlignment = AbsoluteAlignment.Left) {

                Text(
                    text = title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = TextEspresso
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stepText,
                    fontSize = 14.sp,
                    color = TextMuted
                )

                Spacer(Modifier.height(12.dp))

                // Step dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(totalSteps) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (index == activeStep)
                                        MatchaGreen
                                    else
                                        MatchaGreen.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(50)
                                )
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 108.dp) // 👈 reserve space for CTA
            ) {
                content()
            }

        }

        // --- FIXED BOTTOM CTA ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(BgCream)
                .padding(24.dp)
        ) {
            Button(
                onClick = onCtaClick,
                enabled = ctaEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatchaGreen,
                    disabledContainerColor = MatchaGreen.copy(alpha = 0.4f)
                )
            ) {
                Text(ctaText, fontSize = 18.sp)
            }
        }
    }
}
