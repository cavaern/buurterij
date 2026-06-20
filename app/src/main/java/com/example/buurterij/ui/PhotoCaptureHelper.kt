package com.example.buurterij.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.FileProvider
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PhotoThumbnail(filePath: String, modifier: Modifier = Modifier) {
    var bitmap by remember(filePath) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(filePath) {
        bitmap = withContext(Dispatchers.IO) {
            val options = BitmapFactory.Options().apply { inSampleSize = 4 }
            BitmapFactory.decodeFile(filePath, options)?.asImageBitmap()
        }
    }
    bitmap?.let {
        Image(bitmap = it, contentDescription = null, contentScale = ContentScale.Crop, modifier = modifier)
    }
}

object PhotoCaptureHelper {
    private fun photosDir(context: Context): File =
        File(context.filesDir, "photos").apply { mkdirs() }

    fun createImageFile(context: Context): File =
        File(photosDir(context), "IMG_${System.currentTimeMillis()}.jpg")

    fun getUriForFile(context: Context, file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    fun copyContentUriToFile(context: Context, sourceUri: Uri): File {
        val destination = File(photosDir(context), "IMG_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destination.outputStream().use { output -> input.copyTo(output) }
        }
        return destination
    }
}
