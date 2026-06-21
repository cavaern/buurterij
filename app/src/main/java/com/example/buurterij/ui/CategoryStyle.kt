package com.example.buurterij.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.buurterij.R
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.ui.theme.Berry40
import com.example.buurterij.ui.theme.Flower40
import com.example.buurterij.ui.theme.ForestGreen40
import com.example.buurterij.ui.theme.Mushroom40
import com.example.buurterij.ui.theme.Nut40
import com.example.buurterij.ui.theme.Seed40
import com.example.buurterij.ui.theme.Teal40

fun PlantCategory.label(): String = when (this) {
    PlantCategory.BERRY -> "Bessen"
    PlantCategory.HERB -> "Kruiden"
    PlantCategory.NUT -> "Noten"
    PlantCategory.FLOWER -> "Bloemen"
    PlantCategory.MUSHROOM -> "Paddenstoelen"
    PlantCategory.MARKET_STALL -> "Marktkraampjes"
    PlantCategory.SEED -> "Zaden/Stekken"
}

@DrawableRes
fun PlantCategory.markerDrawableRes(): Int = when (this) {
    PlantCategory.BERRY -> R.drawable.marker_berry
    PlantCategory.HERB -> R.drawable.marker_herb
    PlantCategory.NUT -> R.drawable.marker_nut
    PlantCategory.FLOWER -> R.drawable.marker_flower
    PlantCategory.MUSHROOM -> R.drawable.marker_mushroom
    PlantCategory.MARKET_STALL -> R.drawable.marker_market_stand
    PlantCategory.SEED -> R.drawable.marker_seeds
}

@DrawableRes
fun PlantCategory.petalDrawableRes(): Int = when (this) {
    PlantCategory.BERRY -> R.drawable.petal_berry
    PlantCategory.HERB -> R.drawable.petal_herb
    PlantCategory.NUT -> R.drawable.petal_nut
    PlantCategory.FLOWER -> R.drawable.petal_flower
    PlantCategory.MUSHROOM -> R.drawable.petal_mushroom
    PlantCategory.MARKET_STALL -> R.drawable.petal_market_stall
    PlantCategory.SEED -> R.drawable.petal_seed
}

fun PlantCategory.themeColor(): Color = when (this) {
    PlantCategory.BERRY -> Berry40
    PlantCategory.HERB -> ForestGreen40
    PlantCategory.NUT -> Nut40
    PlantCategory.FLOWER -> Flower40
    PlantCategory.MUSHROOM -> Mushroom40
    PlantCategory.MARKET_STALL -> Teal40
    PlantCategory.SEED -> Seed40
}
