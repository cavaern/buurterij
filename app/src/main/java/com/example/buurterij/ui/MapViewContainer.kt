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
import com.example.buurterij.data.Language
import com.example.buurterij.data.displayName
import com.example.buurterij.data.isInSeason
import kotlinx.coroutines.delay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private val NETHERLANDS_CENTER = GeoPoint(52.1326, 5.2913)
private const val INITIAL_ZOOM = 7.0

@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    spots: List<SpotUiModel>,
    hasLocationPermission: Boolean,
    recenterRequest: Int,
    mainLanguage: Language = Language.DUTCH,
    secondaryLanguage: Language? = Language.ENGLISH,
    onMapTap: (GeoPoint) -> Unit,
    onMarkerTap: (SpotUiModel) -> Unit,
    onMyLocationChanged: (GeoPoint?) -> Unit = {},
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
    val locationOverlay = remember { MyLocationNewOverlay(GpsMyLocationProvider(context), mapView) }

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

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            locationOverlay.enableMyLocation()
            if (!mapView.overlays.contains(locationOverlay)) {
                mapView.overlays.add(locationOverlay)
            }
        } else {
            locationOverlay.disableMyLocation()
            mapView.overlays.remove(locationOverlay)
        }
        mapView.invalidate()
    }

    LaunchedEffect(recenterRequest) {
        if (recenterRequest > 0) {
            locationOverlay.myLocation?.let { mapView.controller.animateTo(it) }
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            while (true) {
                onMyLocationChanged(locationOverlay.myLocation)
                delay(1000)
            }
        } else {
            onMyLocationChanged(null)
        }
    }

    LaunchedEffect(spots, mainLanguage, secondaryLanguage) {
        markers.values.forEach { mapView.overlays.remove(it) }
        markers.clear()
        val currentMonth = java.time.LocalDate.now().monthValue
        spots.forEach { spot ->
            val inSeason = spot.plantType.isInSeason(currentMonth)
            val marker = Marker(mapView).apply {
                position = GeoPoint(spot.latitude, spot.longitude)
                title = spot.plantType.displayName(mainLanguage, secondaryLanguage)
                icon = MarkerIconFactory.categoryMarkerDrawable(context, spot.plantType.category, inSeason)
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
            locationOverlay.disableMyLocation()
            mapView.onDetach()
        }
    }

    AndroidView(factory = { mapView }, modifier = modifier.fillMaxSize())
}
