package com.example.buurterij.ui

import androidx.annotation.DrawableRes
import com.example.buurterij.R
import com.example.buurterij.data.PlantCategory

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
