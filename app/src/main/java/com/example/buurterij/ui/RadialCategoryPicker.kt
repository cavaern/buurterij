package com.example.buurterij.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.buurterij.R
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.ui.theme.ForestGreen40
import kotlin.math.cos
import kotlin.math.sin

private val PetalRadius = 112.dp
private val PetalSize = 60.dp
private val CenterSize = 76.dp

@Composable
fun RadialCategoryPicker(
    visible: Boolean,
    onCategorySelected: (PlantCategory) -> Unit,
    onCancel: () -> Unit,
) {
    if (visible) {
        BackHandler(onBack = onCancel)
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onCancel,
                ),
            contentAlignment = Alignment.Center,
        ) {
            val categories = PlantCategory.entries
            val angleStep = 360f / categories.size

            categories.forEachIndexed { index, category ->
                val angleRad = Math.toRadians((angleStep * index - 90).toDouble())
                val offsetX = PetalRadius * cos(angleRad).toFloat()
                val offsetY = PetalRadius * sin(angleRad).toFloat()

                Column(
                    modifier = Modifier.offset(x = offsetX, y = offsetY),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(PetalSize)
                            .clip(CircleShape)
                            .background(category.themeColor())
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { onCategorySelected(category) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(category.markerDrawableRes()),
                            contentDescription = category.label(),
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(PetalSize)
                                .padding(6.dp),
                        )
                    }
                    Text(
                        text = category.label(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(CenterSize)
                    .clip(CircleShape)
                    .background(ForestGreen40)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onCancel,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sprout),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                    Text(
                        text = "Cancel",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}
