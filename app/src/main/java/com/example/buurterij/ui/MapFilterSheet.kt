package com.example.buurterij.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.ui.theme.Cream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapFilterSheet(
    filterState: MapFilterState,
    onFilterStateChange: (MapFilterState) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Cream) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Filter spots", style = MaterialTheme.typography.titleMedium)

            Text(
                text = "Season",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = filterState.showActive,
                    onCheckedChange = { onFilterStateChange(filterState.copy(showActive = it)) },
                )
                Text("In season")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = filterState.showInactive,
                    onCheckedChange = { onFilterStateChange(filterState.copy(showInactive = it)) },
                )
                Text("Out of season")
            }

            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
            )
            PlantCategory.entries.forEach { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Checkbox(
                        checked = category in filterState.selectedCategories,
                        onCheckedChange = { checked ->
                            val updated = if (checked) {
                                filterState.selectedCategories + category
                            } else {
                                filterState.selectedCategories - category
                            }
                            onFilterStateChange(filterState.copy(selectedCategories = updated))
                        },
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(category.themeColor()),
                    )
                    Text(
                        text = category.label(),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}
