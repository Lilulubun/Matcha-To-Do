package com.example.matchatodo.ui.screens

import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.matchatodo.data.Sticker
import com.example.matchatodo.ui.components.RewardFrame
import com.example.matchatodo.ui.theme.*
import com.example.matchatodo.utils.exportViewToCache
import com.example.matchatodo.utils.saveBitmapToCache
import com.example.matchatodo.utils.saveToGallery
import com.example.matchatodo.utils.shareImage

@Composable
fun RewardScreen(
    goalName: String,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var exportedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedStickers by remember { mutableStateOf<List<Sticker>>(emptyList()) }

    // This is THE view that will be exported
    val frameViewRef = remember { mutableStateOf<View?>(null) }
    var isFrameReady by remember { mutableStateOf(false) }


    // CAMERA → Bitmap
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            photoUri = saveBitmapToCache(context, it)
        }
    }

    // GALLERY → Uri
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BgCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ─── HEADER ───
        Text(
            text = "DELICIOUS!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = MatchaGreen
        )

        Text(
            text = "You earned this moment 🍵",
            fontSize = 14.sp,
            color = TextMuted
        )

        Spacer(Modifier.height(20.dp))

        // ─── FRAME (VISIBLE + EXPORTABLE) ───
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f),
            factory = { ctx ->
                FrameLayout(ctx).also { frameViewRef.value = it }
            },
            update = { container ->
                container.removeAllViews()

                val composeView = ComposeView(container.context).apply {
                    setContent {
                        RewardFrame(
                            goalName = goalName,
                            photoUri = photoUri,
                            selectedStickers = selectedStickers,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                container.addView(composeView)

                // 👇 THIS IS CRITICAL
                composeView.viewTreeObserver.addOnGlobalLayoutListener {
                    if (composeView.width > 0 && composeView.height > 0) {
                        isFrameReady = true
                    }
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        // ─── PHOTO INPUT ───
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { cameraLauncher.launch(null) }) {
                Text("Take Photo")
            }
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Gallery")
            }
        }

        Spacer(Modifier.height(16.dp))

        // ─── EXPORT ACTIONS ───
        Button(
            enabled = isFrameReady,
            onClick = {
                frameViewRef.value?.let { view ->
                    exportedUri = exportViewToCache(context, view)
                }
            }
        ) {
            Text("Export Image")
        }

        Button(
            enabled = exportedUri != null,
            onClick = {
                exportedUri?.let { saveToGallery(context, it) }
            }
        ) {
            Text("Save to Gallery")
        }

        Button(
            enabled = exportedUri != null,
            onClick = {
                exportedUri?.let { shareImage(context, it) }
            }
        ) {
            Text("Share")
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Dashboard")
        }
    }
}
