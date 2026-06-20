package com.example.buurterij.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.PlantType
import com.example.buurterij.data.isInSeason

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpotBottomSheet(
    groupedTypes: Map<PlantCategory, List<PlantType>>,
    onDismiss: () -> Unit,
    onPlantTypeSelected: (PlantType) -> Unit,
) {
    val currentMonth = java.time.LocalDate.now().monthValue
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            groupedTypes.forEach { (category, plants) ->
                item {
                    Text(
                        text = category.label(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp),
                    )
                }
                items(plants) { plant ->
                    val inSeason = plant.isInSeason(currentMonth)
                    ListItem(
                        headlineContent = { Text(plant.dutchName) },
                        supportingContent = { Text(plant.englishName) },
                        trailingContent = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (inSeason) Color(0xFF4CAF50) else Color(0xFFBDBDBD)),
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlantTypeSelected(plant) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

