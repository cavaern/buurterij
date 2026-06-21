package com.example.buurterij.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.buurterij.R
import com.example.buurterij.data.LanguagePreferences
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForagingMapScreen(viewModel: ForagingViewModel) {
    val context = LocalContext.current
    val spots by viewModel.spots.collectAsState()
    val groupedTypes by viewModel.allPlantTypes.collectAsState()

    val languagePreferences = remember { LanguagePreferences(context) }
    var mainLanguage by remember { mutableStateOf(languagePreferences.mainLanguage) }
    var secondaryLanguage by remember { mutableStateOf(languagePreferences.secondaryLanguage) }

    var pendingTapLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var selectedSpot by remember { mutableStateOf<SpotUiModel?>(null) }
    var changeTypeForSpot by remember { mutableStateOf<SpotUiModel?>(null) }
    var showManageTypes by remember { mutableStateOf(false) }
    var showLanguageSettings by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var recenterRequest by remember { mutableStateOf(0) }
    var currentLocation by remember { mutableStateOf<GeoPoint?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasLocationPermission = granted
    }

    if (!hasLocationPermission) {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("buurterij") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(painterResource(R.drawable.ic_more_vert), contentDescription = "Menu")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Manage forage types") },
                            onClick = {
                                showMenu = false
                                showManageTypes = true
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("Display languages") },
                            onClick = {
                                showMenu = false
                                showLanguageSettings = true
                            },
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            if (hasLocationPermission) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FloatingActionButton(
                        onClick = { currentLocation?.let { pendingTapLocation = it } },
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_add_location),
                            contentDescription = "Add spot at my location",
                            tint = LocalContentColor.current.copy(alpha = if (currentLocation != null) 1f else 0.38f),
                        )
                    }
                    FloatingActionButton(onClick = { recenterRequest++ }) {
                        Icon(painterResource(R.drawable.ic_my_location), contentDescription = "Center on my location")
                    }
                }
            }
        },
    ) { innerPadding ->
        MapViewContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            spots = spots,
            hasLocationPermission = hasLocationPermission,
            recenterRequest = recenterRequest,
            mainLanguage = mainLanguage,
            secondaryLanguage = secondaryLanguage,
            onMapTap = { pendingTapLocation = it },
            onMarkerTap = { selectedSpot = it },
            onMyLocationChanged = { currentLocation = it },
        )
    }

    pendingTapLocation?.let { location ->
        AddSpotBottomSheet(
            groupedTypes = groupedTypes,
            mainLanguage = mainLanguage,
            secondaryLanguage = secondaryLanguage,
            onDismiss = { pendingTapLocation = null },
            onPlantTypeSelected = { plantType ->
                viewModel.addSpot(location.latitude, location.longitude, plantType.id)
                pendingTapLocation = null
            },
        )
    }

    changeTypeForSpot?.let { spot ->
        AddSpotBottomSheet(
            groupedTypes = groupedTypes,
            mainLanguage = mainLanguage,
            secondaryLanguage = secondaryLanguage,
            onDismiss = { changeTypeForSpot = null },
            onPlantTypeSelected = { plantType ->
                viewModel.updatePlantType(spot.id, plantType.id)
                changeTypeForSpot = null
            },
        )
    }

    selectedSpot?.let { spot ->
        val photos by viewModel.photosForSpot(spot.id).collectAsState(initial = emptyList())
        SpotDetailBottomSheet(
            spot = spot,
            photos = photos,
            mainLanguage = mainLanguage,
            secondaryLanguage = secondaryLanguage,
            onDismiss = { selectedSpot = null },
            onMarkVisited = {
                viewModel.markVisited(it.id)
                selectedSpot = null
            },
            onDelete = {
                viewModel.deleteSpot(it.id)
                selectedSpot = null
            },
            onSaveNotes = { spotId, notes -> viewModel.updateNotes(spotId, notes) },
            onChangeTypeRequested = {
                changeTypeForSpot = spot
                selectedSpot = null
            },
            onAddPhoto = { filePath -> viewModel.addPhoto(spot.id, filePath) },
        )
    }

    if (showManageTypes) {
        ManageForageTypesScreen(
            groupedTypes = groupedTypes,
            mainLanguage = mainLanguage,
            secondaryLanguage = secondaryLanguage,
            onDismiss = { showManageTypes = false },
            onAddType = { dutchName, englishName, category, start, end ->
                viewModel.addCustomType(dutchName, englishName, category, start, end)
            },
            onUpdateType = { id, dutchName, englishName, category, start, end ->
                viewModel.updateCustomType(id, dutchName, englishName, category, start, end)
            },
            onDeleteType = { id, onBlocked ->
                viewModel.deleteCustomType(id, onBlocked)
            },
        )
    }

    if (showLanguageSettings) {
        LanguageSettingsScreen(
            initialMain = mainLanguage,
            initialSecondary = secondaryLanguage,
            onDismiss = { showLanguageSettings = false },
            onSave = { main, secondary ->
                mainLanguage = main
                secondaryLanguage = secondary
                languagePreferences.mainLanguage = main
                languagePreferences.secondaryLanguage = secondary
                showLanguageSettings = false
            },
        )
    }
}
