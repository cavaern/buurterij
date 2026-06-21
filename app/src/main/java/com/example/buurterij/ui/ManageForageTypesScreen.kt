package com.example.buurterij.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.PlantType
import com.example.buurterij.data.isCustomPlantTypeId
import com.example.buurterij.data.toCustomPlantTypeDbId

private fun PlantType.isCustom(): Boolean = id.isCustomPlantTypeId()

private fun PlantType.customId(): Long? = id.toCustomPlantTypeDbId()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageForageTypesScreen(
    groupedTypes: Map<PlantCategory, List<PlantType>>,
    onDismiss: () -> Unit,
    onAddType: (dutchName: String, englishName: String, category: PlantCategory, seasonStart: Int, seasonEnd: Int) -> Unit,
    onUpdateType: (
        id: Long,
        dutchName: String,
        englishName: String,
        category: PlantCategory,
        seasonStart: Int,
        seasonEnd: Int,
    ) -> Unit,
    onDeleteType: (id: Long, onBlocked: () -> Unit) -> Unit,
) {
    var showAddForm by remember { mutableStateOf(false) }
    var editingType by remember { mutableStateOf<PlantType?>(null) }
    var deleteBlockedType by remember { mutableStateOf<PlantType?>(null) }

    val plantBeingEdited = editingType
    ModalBottomSheet(onDismissRequest = onDismiss) {
        when {
            showAddForm -> {
                AddForageTypeForm(
                    title = "New forage type",
                    onCancel = { showAddForm = false },
                    onSubmit = { dutchName, englishName, category, start, end ->
                        onAddType(dutchName, englishName, category, start, end)
                        showAddForm = false
                    },
                )
            }
            plantBeingEdited != null -> {
                val plant = plantBeingEdited
                AddForageTypeForm(
                    title = "Edit forage type",
                    initialDutchName = plant.dutchName,
                    initialEnglishName = plant.englishName,
                    initialCategory = plant.category,
                    initialSeasonStart = plant.seasonStartMonth,
                    initialSeasonEnd = plant.seasonEndMonth,
                    onCancel = { editingType = null },
                    onSubmit = { dutchName, englishName, category, start, end ->
                        plant.customId()?.let { id ->
                            onUpdateType(id, dutchName, englishName, category, start, end)
                        }
                        editingType = null
                    },
                )
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Button(
                            onClick = { showAddForm = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Text("Add new forage type")
                        }
                    }
                    groupedTypes.forEach { (category, plants) ->
                        item {
                            Text(
                                text = category.label(),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 4.dp),
                            )
                        }
                        items(plants) { plant ->
                            ListItem(
                                headlineContent = { Text(plant.dutchName) },
                                supportingContent = { Text(plant.englishName) },
                                trailingContent = if (plant.isCustom()) {
                                    {
                                        Row {
                                            TextButton(onClick = { editingType = plant }) {
                                                Text("Edit")
                                            }
                                            TextButton(onClick = {
                                                plant.customId()?.let { id ->
                                                    onDeleteType(id) { deleteBlockedType = plant }
                                                }
                                            }) {
                                                Text("Delete")
                                            }
                                        }
                                    }
                                } else {
                                    null
                                },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    deleteBlockedType?.let { plant ->
        AlertDialog(
            onDismissRequest = { deleteBlockedType = null },
            title = { Text("Can't delete ${plant.dutchName}") },
            text = { Text("This forage type is still used by one or more spots. Change those spots' type first.") },
            confirmButton = {
                TextButton(onClick = { deleteBlockedType = null }) {
                    Text("OK")
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddForageTypeForm(
    onCancel: () -> Unit,
    onSubmit: (dutchName: String, englishName: String, category: PlantCategory, seasonStart: Int, seasonEnd: Int) -> Unit,
    title: String = "New forage type",
    initialDutchName: String = "",
    initialEnglishName: String = "",
    initialCategory: PlantCategory = PlantCategory.BERRY,
    initialSeasonStart: Int = 1,
    initialSeasonEnd: Int = 12,
) {
    var dutchName by remember { mutableStateOf(initialDutchName) }
    var englishName by remember { mutableStateOf(initialEnglishName) }
    var category by remember { mutableStateOf(initialCategory) }
    var seasonStart by remember { mutableStateOf(initialSeasonStart) }
    var seasonEnd by remember { mutableStateOf(initialSeasonEnd) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = dutchName,
            onValueChange = { dutchName = it },
            label = { Text("Dutch name") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = englishName,
            onValueChange = { englishName = it },
            label = { Text("English name") },
            modifier = Modifier.fillMaxWidth(),
        )

        EnumDropdown(
            label = "Category",
            options = PlantCategory.entries,
            selected = category,
            displayText = { it.label() },
            onSelected = { category = it },
        )

        EnumDropdown(
            label = "Season start month",
            options = (1..12).toList(),
            selected = seasonStart,
            displayText = { it.toString() },
            onSelected = { seasonStart = it },
        )
        EnumDropdown(
            label = "Season end month",
            options = (1..12).toList(),
            selected = seasonEnd,
            displayText = { it.toString() },
            onSelected = { seasonEnd = it },
        )

        Button(
            onClick = { onSubmit(dutchName, englishName, category, seasonStart, seasonEnd) },
            enabled = dutchName.isNotBlank() && englishName.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }
        OutlinedButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdown(
    label: String,
    options: List<T>,
    selected: T,
    displayText: (T) -> String,
    onSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = displayText(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(displayText(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}
