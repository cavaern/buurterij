package com.example.buurterij.ui

import androidx.compose.ui.graphics.Color
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.ui.theme.Berry40
import com.example.buurterij.ui.theme.Flower40
import com.example.buurterij.ui.theme.ForestGreen40
import com.example.buurterij.ui.theme.Nut40

fun PlantCategory.label(): String = when (this) {
    PlantCategory.BERRY -> "Bessen"
    PlantCategory.HERB -> "Kruiden"
    PlantCategory.NUT -> "Noten"
    PlantCategory.FLOWER -> "Bloemen"
}

fun PlantCategory.markerColor(): Color = when (this) {
    PlantCategory.BERRY -> Berry40
    PlantCategory.HERB -> ForestGreen40
    PlantCategory.NUT -> Nut40
    PlantCategory.FLOWER -> Flower40
}
