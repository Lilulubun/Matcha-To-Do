package com.example.matchatodo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.content.ContentValues
import androidx.core.content.FileProvider
import android.view.View
import android.graphics.Bitmap
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream

/* -------------------------------------------
   EXPORT COMPOSE VIEW → CACHE PNG
-------------------------------------------- */
fun exportViewToCache(
    context: Context,
    view: View
): Uri {
    val bitmap = view.drawToBitmap()

    val file = File(
        context.cacheDir,
        "reward_${System.currentTimeMillis()}.png"
    )

    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}


/* -------------------------------------------
   SAVE IMAGE TO GALLERY
-------------------------------------------- */
fun saveToGallery(
    context: Context,
    sourceUri: Uri
) {
    val resolver = context.contentResolver
    val inputStream = resolver.openInputStream(sourceUri) ?: return

    val values = ContentValues().apply {
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "Matcha_${System.currentTimeMillis()}.png"
        )
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MatchaToDo")
    }

    val galleryUri =
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: return

    resolver.openOutputStream(galleryUri)?.use { output ->
        inputStream.copyTo(output)
    }
}

/* -------------------------------------------
   SHARE IMAGE (INTENT)
-------------------------------------------- */
fun shareImage(
    context: Context,
    uri: Uri
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(intent, "Share your Matcha 🍵")
    )
}
fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(
        context.cacheDir,
        "camera_${System.currentTimeMillis()}.png"
    )

    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}
