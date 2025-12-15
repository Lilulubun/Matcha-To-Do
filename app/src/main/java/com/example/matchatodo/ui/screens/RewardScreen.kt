package com.example.matchatodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.ui.theme.*

@Composable
fun RewardScreen(
    onBackToHome: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().background(BgCream).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("DELICIOUS!", fontSize = 40.sp, fontWeight = FontWeight.Black, color = MatchaGreen)
        Text("Goal Completed", fontSize = 20.sp, color = TextMuted)

        Spacer(Modifier.height(32.dp))

        // Full Cup Animation
        MatchaCupCharacter(1, 1, Modifier.size(200.dp, 280.dp))

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TextEspresso)
        ) {
            Text("Back to Menu")
        }
    }
}