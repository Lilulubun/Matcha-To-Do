package com.example.matchatodo.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.matchatodo.data.Sticker
import com.example.matchatodo.ui.components.StickerPicker
import com.example.matchatodo.ui.components.availableStickers
import com.example.matchatodo.ui.theme.*
import java.io.File
import java.io.FileOutputStream

/* ---------------------------------------------------
   HELPER: SAVE CAMERA BITMAP → URI
--------------------------------------------------- */
private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "reward_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

/* ---------------------------------------------------
   FRAME COMPOSABLE (EXPORT-READY)
--------------------------------------------------- */
@Composable
private fun RewardFrame(
    goalName: String,
    photoUri: Uri?,
    selectedStickers: List<Sticker>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(3f / 4f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(2.dp, TextEspresso, RoundedCornerShape(24.dp))
    ) {

        // ─── PHOTO BASE ───
        if (photoUri != null) {
            AsyncImage(
                model = photoUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TextMuted.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text("Your photo here 📸", color = TextMuted)
            }
        }

        // ─── TEXT STICKERS (ON PHOTO) ───
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // white sticker
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "I GOT MY MATCHA BY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextEspresso
                )
            }

            Spacer(Modifier.height(6.dp))

            // green sticker (goal name)
            Box(
                modifier = Modifier
                    .background(MatchaGreen, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = goalName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    maxLines = 1
                )
            }
        }

        // ─── LEFT STICKER (−21°) ───
        selectedStickers.getOrNull(0)?.let { sticker ->
            Image(
                painter = painterResource(sticker.resId),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .graphicsLayer { rotationZ = -21f }
            )
        }

        // ─── RIGHT STICKER (+21°) ───
        selectedStickers.getOrNull(1)?.let { sticker ->
            Image(
                painter = painterResource(sticker.resId),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .graphicsLayer { rotationZ = 21f }
            )
        }
    }
}

/* ---------------------------------------------------
   MAIN SCREEN
--------------------------------------------------- */
@Composable
fun RewardScreen(
    goalName: String,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedStickers by remember { mutableStateOf<List<Sticker>>(emptyList()) }

    // toggle logic (max 2)
    fun toggleSticker(sticker: Sticker) {
        selectedStickers =
            if (selectedStickers.contains(sticker)) {
                selectedStickers - sticker
            } else if (selectedStickers.size < 2) {
                selectedStickers + sticker
            } else {
                selectedStickers
            }
    }

    // CAMERA
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            photoUri = saveBitmapToCache(context, it)
        }
    }

    // GALLERY
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(12.dp))

        Text(
            text = "DELICIOUS!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = MatchaGreen
        )

        Text(
            text = "You earned this moment 🍵",
            fontSize = 16.sp,
            color = TextMuted
        )

        Spacer(Modifier.height(24.dp))

        RewardFrame(
            goalName = goalName,
            photoUri = photoUri,
            selectedStickers = selectedStickers,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        StickerPicker(
            stickers = availableStickers,
            selected = selectedStickers,
            onToggle = ::toggleSticker
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { cameraLauncher.launch(null) },
                colors = ButtonDefaults.buttonColors(containerColor = MatchaGreen)
            ) {
                Text("Take Photo")
            }

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = MatchaGreen)
            ) {
                Text("Gallery")
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onBackToHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TextEspresso)
        ) {
            Text("Back to Dashboard")
        }
    }
}

/* ---------------------------------------------------
   PREVIEW
--------------------------------------------------- */
@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
fun RewardScreenPreview() {
    RewardScreen(
        goalName = "Read 5 Books",
        onBackToHome = {}
    )
}
