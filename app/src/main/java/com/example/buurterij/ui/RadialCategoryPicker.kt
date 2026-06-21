package com.example.buurterij.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.buurterij.R
import com.example.buurterij.data.PlantCategory
import kotlin.math.cos
import kotlin.math.sin

private val PetalRadius = 116.dp
private val PetalWidth = 76.dp
private val PetalHeight = 100.dp
private val CenterSize = 100.dp

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

                Image(
                    painter = painterResource(category.petalDrawableRes()),
                    contentDescription = category.label(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(x = offsetX, y = offsetY)
                        .size(width = PetalWidth, height = PetalHeight)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onCategorySelected(category) },
                )
            }

            Image(
                painter = painterResource(R.drawable.petal_cancel),
                contentDescription = "Cancel",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(CenterSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onCancel,
                    ),
            )
        }
    }
}
