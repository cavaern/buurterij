package com.example.buurterij.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay

private val NETHERLANDS_CENTER = GeoPoint(52.1326, 5.2913)
private const val INITIAL_ZOOM = 7.0

@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    spots: List<SpotUiModel>,
    onMapTap: (GeoPoint) -> Unit,
    onMarkerTap: (SpotUiModel) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(INITIAL_ZOOM)
            controller.setCenter(NETHERLANDS_CENTER)
        }
    }

    val markers = remember { mutableMapOf<Long, Marker>() }

    LaunchedEffect(mapView) {
        val receiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                onMapTap(p)
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean = false
        }
        mapView.overlays.add(0, MapEventsOverlay(receiver))
    }

    LaunchedEffect(spots) {
        markers.values.forEach { mapView.overlays.remove(it) }
        markers.clear()
        spots.forEach { spot ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(spot.latitude, spot.longitude)
                title = spot.plantType.dutchName
                snippet = spot.plantType.englishName
                setOnMarkerClickListener { _, _ ->
                    onMarkerTap(spot)
                    true
                }
            }
            markers[spot.id] = marker
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    AndroidView(factory = { mapView }, modifier = modifier.fillMaxSize())
}
