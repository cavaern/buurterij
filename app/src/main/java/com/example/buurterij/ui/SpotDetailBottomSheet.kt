package com.example.buurterij.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.isInSeason
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
    onDismiss: () -> Unit,
    onMarkVisited: (SpotUiModel) -> Unit,
    onDelete: (SpotUiModel) -> Unit,
) {
    val plantType = spot.plantType
    val currentMonth = java.time.LocalDate.now().monthValue
    val inSeason = plantType.isInSeason(currentMonth)
    val seasonText = "${monthNames[plantType.seasonStartMonth - 1]} - ${monthNames[plantType.seasonEndMonth - 1]}"
    val lastVisitedText = spot.lastVisitedAt?.let { millis ->
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
    } ?: "Never visited"

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = plantType.dutchName, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "${plantType.englishName} · ${if (plantType.category == PlantCategory.BERRY) "Bessen" else "Kruiden"}",
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = { onMarkVisited(spot) }) {
                    Text("Mark visited today")
                }
                OutlinedButton(onClick = { onDelete(spot) }) {
                    Text("Delete spot")
                }
            }
        }
    }
}
