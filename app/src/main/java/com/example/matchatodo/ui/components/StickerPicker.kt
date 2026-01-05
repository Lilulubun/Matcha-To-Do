package com.example.matchatodo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.matchatodo.data.Sticker
import com.example.matchatodo.ui.theme.MatchaGreen

@Composable
fun StickerPicker(
    stickers: List<Sticker>,
    selected: List<Sticker>,
    onToggle: (Sticker) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stickers.forEach { sticker ->
            Image(
                painter = painterResource(sticker.resId),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .border(
                        width = if (selected.contains(sticker)) 2.dp else 0.dp,
                        color = MatchaGreen,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onToggle(sticker) }
            )
        }
    }
}
