package com.example.buurterij.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.PlantCatalog
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.PlantType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpotBottomSheet(
    onDismiss: () -> Unit,
    onPlantTypeSelected: (PlantType) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            PlantCatalog.groupedByCategory().forEach { (category, plants) ->
                item {
                    Text(
                        text = if (category == PlantCategory.BERRY) "Bessen" else "Kruiden",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp),
                    )
                }
                items(plants) { plant ->
                    ListItem(
                        headlineContent = { Text(plant.dutchName) },
                        supportingContent = { Text(plant.englishName) },
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
