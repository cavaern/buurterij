package com.example.buurterij.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.osmdroid.util.GeoPoint

@Composable
fun ForagingMapScreen(viewModel: ForagingViewModel) {
    val spots by viewModel.spots.collectAsState()
    var pendingTapLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var selectedSpot by remember { mutableStateOf<SpotUiModel?>(null) }

    MapViewContainer(
        modifier = Modifier.fillMaxSize(),
        spots = spots,
        onMapTap = { pendingTapLocation = it },
        onMarkerTap = { selectedSpot = it },
    )

    pendingTapLocation?.let { location ->
        AddSpotBottomSheet(
            onDismiss = { pendingTapLocation = null },
            onPlantTypeSelected = { plantType ->
                viewModel.addSpot(location.latitude, location.longitude, plantType.id)
                pendingTapLocation = null
            },
        )
    }

    selectedSpot?.let { spot ->
        SpotDetailBottomSheet(
            spot = spot,
            onDismiss = { selectedSpot = null },
            onMarkVisited = {
                viewModel.markVisited(it.id)
                selectedSpot = null
            },
            onDelete = {
                viewModel.deleteSpot(it.id)
                selectedSpot = null
            },
        )
    }
}
