package com.example.matchatodo.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
// --- ADD IMPORTS ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star // using Star instead of LocalCafe
import androidx.compose.material3.Icon

// --- ADD THIS FUNCTION AT THE BOTTOM ---
@Composable
fun MatchaMascotIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Matcha Mascot",
        tint = MatchaGreen,
        modifier = modifier
    )
}

// --- COLORS ---
val BgCream = Color(0xFFFDFCF0)
val MatchaGreen = Color(0xFF8A9A5B)
val TextEspresso = Color(0xFF2D2D2D)
val TextMuted = Color(0xFF5D534A)
val Terracotta = Color(0xFFD69F8B)
val LightFoam = Color(0xFFB8C89B)
val White = Color(0xFFFFFFFF)

// --- MATCHA CUP CHARACTER ---
@Composable
fun MatchaCupCharacter(completedCount: Int, totalCount: Int, modifier: Modifier = Modifier) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val isComplete = progress >= 1f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000), label = "fill")

    Canvas(modifier = modifier) {
        val scaleX = size.width / 200f; val scaleY = size.height / 280f
        withTransform({ scale(scaleX, scaleY, Offset.Zero) }) {
            // Cup Shape & Handle
            val cupPath = Path().apply { moveTo(60f, 40f); lineTo(50f, 220f); quadraticBezierTo(50f, 240f, 70f, 240f); lineTo(130f, 240f); quadraticBezierTo(150f, 240f, 150f, 220f); lineTo(140f, 40f); close() }
            val handle = Path().apply { moveTo(150f, 80f); quadraticBezierTo(185f, 100f, 185f, 140f); quadraticBezierTo(185f, 180f, 150f, 200f) }
            val handleIn = Path().apply { moveTo(150f, 92f); quadraticBezierTo(172f, 106f, 172f, 140f); quadraticBezierTo(172f, 174f, 150f, 188f) }

            drawPath(handle, TextEspresso, style = Stroke(4.5f, cap = StrokeCap.Round))
            drawPath(handleIn, TextEspresso, style = Stroke(4.5f, cap = StrokeCap.Round))
            drawPath(cupPath, Color(0xFFE8E6D8)) // Bg

            // Liquid
            clipPath(cupPath) {
                drawRect(Terracotta.copy(0.15f), topLeft = Offset(45f, 40f), size = Size(110f, 200f))
                if (animatedProgress > 0) {
                    val h = 200f * animatedProgress
                    drawRect(MatchaGreen.copy(0.9f), topLeft = Offset(45f, 240f - h), size = Size(110f, h))
                    drawOval(LightFoam.copy(0.7f), topLeft = Offset(48f, 240f - h - 10f), size = Size(104f, 20f))
                }
            }
            drawPath(cupPath, TextEspresso, style = Stroke(4.5f, cap = StrokeCap.Round))
            drawOval(TextEspresso, topLeft = Offset(60f, 30f), size = Size(80f, 20f), style = Stroke(4.5f))

            // Face
            val eyeY = 130f
            if (isComplete) {
                val happyL = Path().apply { moveTo(82f, 130f); quadraticBezierTo(85f, 135f, 88f, 130f) }
                val happyR = Path().apply { moveTo(112f, 130f); quadraticBezierTo(115f, 135f, 118f, 130f) }
                drawPath(happyL, TextEspresso, style = Stroke(4f, cap=StrokeCap.Round)); drawPath(happyR, TextEspresso, style = Stroke(4f, cap=StrokeCap.Round))
                val smile = Path().apply { moveTo(85f, 150f); quadraticBezierTo(100f, 160f, 115f, 150f) }
                drawPath(smile, TextEspresso, style = Stroke(3.5f, cap = StrokeCap.Round))
            } else {
                drawCircle(TextEspresso, 3.5f, Offset(85f, eyeY)); drawCircle(TextEspresso, 3.5f, Offset(115f, eyeY))
                val smile = Path().apply { moveTo(90f, 150f); quadraticBezierTo(100f, 155f, 110f, 150f) }
                drawPath(smile, TextEspresso, style = Stroke(3f, cap = StrokeCap.Round))
            }
        }
    }
}