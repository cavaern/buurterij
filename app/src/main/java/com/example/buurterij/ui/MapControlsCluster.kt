package com.example.buurterij.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.buurterij.R

private val ControlSize = 56.dp

@Composable
private fun ControlButton(
    iconRes: Int,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Image(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(ControlSize)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
    )
}

@Composable
fun MapControlsCluster(
    hasLocationPermission: Boolean,
    hasPendingLocation: Boolean,
    onCenterOnMe: () -> Unit,
    onAddDiscovery: () -> Unit,
) {
    if (!hasLocationPermission) return

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        ControlButton(
            iconRes = R.drawable.control_my_location,
            contentDescription = "Center on my location",
            enabled = true,
            onClick = onCenterOnMe,
        )
        ControlButton(
            iconRes = R.drawable.control_add_discovery,
            contentDescription = "Add a new find",
            enabled = hasPendingLocation,
            onClick = onAddDiscovery,
        )
        ControlButton(
            iconRes = R.drawable.control_filter,
            contentDescription = "Filter categories",
            enabled = false,
            onClick = {},
        )
        ControlButton(
            iconRes = R.drawable.control_search,
            contentDescription = "Search places",
            enabled = false,
            onClick = {},
        )
        ControlButton(
            iconRes = R.drawable.control_layers,
            contentDescription = "Map layers",
            enabled = false,
            onClick = {},
        )
    }
}
