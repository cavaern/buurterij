package com.example.buurterij.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.Language
import com.example.buurterij.data.SpotPhotoEntity
import com.example.buurterij.data.displayName
import com.example.buurterij.data.isInSeason
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
private val monthNames = (1..12).map {
    java.time.Month.of(it).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotDetailBottomSheet(
    spot: SpotUiModel,
    photos: List<SpotPhotoEntity>,
    mainLanguage: Language = Language.DUTCH,
    secondaryLanguage: Language? = Language.ENGLISH,
    onDismiss: () -> Unit,
    onMarkVisited: (SpotUiModel) -> Unit,
    onDelete: (SpotUiModel) -> Unit,
    onSaveNotes: (Long, String) -> Unit,
    onChangeTypeRequested: () -> Unit,
    onAddPhoto: (String) -> Unit,
) {
    val context = LocalContext.current
    val plantType = spot.plantType
    val currentMonth = java.time.LocalDate.now().monthValue
    val inSeason = plantType.isInSeason(currentMonth)
    val seasonText = "${monthNames[plantType.seasonStartMonth - 1]} - ${monthNames[plantType.seasonEndMonth - 1]}"
    val lastVisitedText = spot.lastVisitedAt?.let { millis ->
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
    } ?: "Never visited"

    var notesDraft by remember(spot.id) { mutableStateOf(spot.notes ?: "") }
    var pendingCameraFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            pendingCameraFile?.let { onAddPhoto(it.absolutePath) }
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val file = PhotoCaptureHelper.copyContentUriToFile(context, uri)
            onAddPhoto(file.absolutePath)
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = plantType.displayName(mainLanguage, secondaryLanguage),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = plantType.category.label(),
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssistChip(onClick = {}, label = { Text("Season: $seasonText") })
                if (inSeason) {
                    AssistChip(onClick = {}, label = { Text("In season now") })
                }
            }

            Text(
                text = "Last visited: $lastVisitedText",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp),
            )

            OutlinedTextField(
                value = notesDraft,
                onValueChange = { notesDraft = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            )
            Button(
                onClick = { onSaveNotes(spot.id, notesDraft) },
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("Save notes")
            }

            Text(
                text = "Photos",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp),
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                items(photos, key = { it.id }) { photo ->
                    PhotoThumbnail(
                        filePath = photo.filePath,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(onClick = {
                    val file = PhotoCaptureHelper.createImageFile(context)
                    pendingCameraFile = file
                    cameraLauncher.launch(PhotoCaptureHelper.getUriForFile(context, file))
                }) {
                    Text("Take photo")
                }
                OutlinedButton(onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    Text("Choose photo")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = { onMarkVisited(spot) }) {
                    Text("Mark visited today")
                }
                OutlinedButton(onClick = onChangeTypeRequested) {
                    Text("Change type")
                }
            }
            OutlinedButton(
                onClick = { onDelete(spot) },
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("Delete spot")
            }
        }
    }
}
