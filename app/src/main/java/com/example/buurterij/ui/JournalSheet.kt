package com.example.buurterij.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.buurterij.data.JournalEntryEntity
import com.example.buurterij.ui.theme.Cream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalSheet(
    entries: List<JournalEntryEntity>,
    onDismiss: () -> Unit,
    onAddEntry: (title: String, content: String) -> Unit,
    onUpdateEntry: (id: Long, title: String, content: String) -> Unit,
    onDeleteEntry: (id: Long) -> Unit,
) {
    var showAddForm by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<JournalEntryEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val entryBeingEdited = editingEntry
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Cream) {
        when {
            showAddForm -> {
                JournalEntryForm(
                    title = "New note",
                    onCancel = { showAddForm = false },
                    onSubmit = { noteTitle, noteContent ->
                        onAddEntry(noteTitle, noteContent)
                        showAddForm = false
                    },
                )
            }
            entryBeingEdited != null -> {
                JournalEntryForm(
                    title = "Edit note",
                    initialTitle = entryBeingEdited.title,
                    initialContent = entryBeingEdited.content,
                    showDelete = true,
                    onCancel = { editingEntry = null },
                    onSubmit = { noteTitle, noteContent ->
                        onUpdateEntry(entryBeingEdited.id, noteTitle, noteContent)
                        editingEntry = null
                    },
                    onDelete = {
                        onDeleteEntry(entryBeingEdited.id)
                        editingEntry = null
                    },
                )
            }
            else -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Journal", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search notes by name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 8.dp),
                    )

                    Button(
                        onClick = { showAddForm = true },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Add new note")
                    }
                }

                val filteredEntries = entries.filter { it.title.contains(searchQuery, ignoreCase = true) }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredEntries, key = { it.id }) { entry ->
                        ListItem(
                            headlineContent = { Text(entry.title) },
                            supportingContent = {
                                Text(
                                    text = entry.content.take(80),
                                    maxLines = 1,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { editingEntry = entry },
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
private fun JournalEntryForm(
    onCancel: () -> Unit,
    onSubmit: (title: String, content: String) -> Unit,
    title: String = "New note",
    initialTitle: String = "",
    initialContent: String = "",
    showDelete: Boolean = false,
    onDelete: () -> Unit = {},
) {
    var noteTitle by remember { mutableStateOf(initialTitle) }
    var noteContent by remember { mutableStateOf(initialContent) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = noteTitle,
            onValueChange = { noteTitle = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = noteContent,
            onValueChange = { noteContent = it },
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onSubmit(noteTitle, noteContent) },
                enabled = noteTitle.isNotBlank(),
            ) {
                Text("Save")
            }
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
        if (showDelete) {
            OutlinedButton(onClick = onDelete) {
                Text("Delete note")
            }
        }
    }
}
