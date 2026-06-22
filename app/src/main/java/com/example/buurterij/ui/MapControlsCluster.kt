package com.example.buurterij.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.buurterij.R
import com.example.buurterij.ui.theme.Cream
import com.example.buurterij.ui.theme.CreamOutline
import com.example.buurterij.ui.theme.ForestGreen40

private val ControlSize = 56.dp

@Composable
private fun ControlButton(
    iconRes: Int,
    contentDescription: String,
    enabled: Boolean,
    active: Boolean = false,
    onClick: () -> Unit,
) {
    Image(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(ControlSize)
            .clip(CircleShape)
            .then(
                if (active) {
                    Modifier.border(3.dp, ForestGreen40, CircleShape)
                } else {
                    Modifier
                },
            )
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
    )
}

@Composable
fun MapControlsCluster(
    hasLocationPermission: Boolean,
    hasPendingLocation: Boolean,
    isSearchActive: Boolean,
    onCenterOnMe: () -> Unit,
    onAddDiscovery: () -> Unit,
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
    onJournalClick: () -> Unit,
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
            contentDescription = "Filter spots",
            enabled = true,
            onClick = onFilterClick,
        )
        ControlButton(
            iconRes = R.drawable.control_search,
            contentDescription = "Search spots",
            enabled = true,
            active = isSearchActive,
            onClick = onSearchClick,
        )
        ControlButton(
            iconRes = R.drawable.control_journal,
            contentDescription = "Journal",
            enabled = true,
            onClick = onJournalClick,
        )
    }
}

@Composable
fun MoreMenuButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(ControlSize)
            .clip(CircleShape)
            .background(Cream)
            .border(1.dp, CreamOutline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_more_vert),
            contentDescription = "Menu",
            tint = ForestGreen40,
            modifier = Modifier.size(28.dp),
        )
    }
}
