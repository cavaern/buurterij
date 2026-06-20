package com.example.buurterij.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.core.content.ContextCompat
import com.example.buurterij.R
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForagingMapScreen(viewModel: ForagingViewModel) {
    val context = LocalContext.current
    val spots by viewModel.spots.collectAsState()
    val groupedTypes by viewModel.allPlantTypes.collectAsState()

    var pendingTapLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var selectedSpot by remember { mutableStateOf<SpotUiModel?>(null) }
    var changeTypeForSpot by remember { mutableStateOf<SpotUiModel?>(null) }
    var showManageTypes by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var recenterRequest by remember { mutableStateOf(0) }

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
                    }
                },
            )
        },
        floatingActionButton = {
            if (hasLocationPermission) {
                FloatingActionButton(onClick = { recenterRequest++ }) {
                    Icon(painterResource(R.drawable.ic_my_location), contentDescription = "Center on my location")
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
            onMapTap = { pendingTapLocation = it },
            onMarkerTap = { selectedSpot = it },
        )
    }

    pendingTapLocation?.let { location ->
        AddSpotBottomSheet(
            groupedTypes = groupedTypes,
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
            onDismiss = { showManageTypes = false },
            onAddType = { dutchName, englishName, category, start, end ->
                viewModel.addCustomType(dutchName, englishName, category, start, end)
            },
        )
    }
}
