package com.example.buurterij.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buurterij.data.Language
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.PlantType
import com.example.buurterij.data.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageForageTypesScreen(
    groupedTypes: Map<PlantCategory, List<PlantType>>,
    mainLanguage: Language = Language.DUTCH,
    secondaryLanguage: Language? = Language.ENGLISH,
    onDismiss: () -> Unit,
    onAddType: (dutchName: String, englishName: String, category: PlantCategory, seasonStart: Int, seasonEnd: Int) -> Unit,
) {
    var showAddForm by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        if (showAddForm) {
            AddForageTypeForm(
                onCancel = { showAddForm = false },
                onSubmit = { dutchName, englishName, category, start, end ->
                    onAddType(dutchName, englishName, category, start, end)
                    showAddForm = false
                },
            )
        } else {
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
                            headlineContent = { Text(plant.displayName(mainLanguage, secondaryLanguage)) },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddForageTypeForm(
    onCancel: () -> Unit,
    onSubmit: (dutchName: String, englishName: String, category: PlantCategory, seasonStart: Int, seasonEnd: Int) -> Unit,
) {
    var dutchName by remember { mutableStateOf("") }
    var englishName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(PlantCategory.BERRY) }
    var seasonStart by remember { mutableStateOf(1) }
    var seasonEnd by remember { mutableStateOf(12) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("New forage type", style = MaterialTheme.typography.titleMedium)

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
