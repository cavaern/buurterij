package com.example.buurterij.ui

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.buurterij.R
import com.example.buurterij.data.PlantCategory

private val OUT_OF_SEASON_TINT = AndroidColor.argb(255, 158, 158, 158)

object MarkerIconFactory {
    fun categoryMarkerDrawable(context: Context, category: PlantCategory, inSeason: Boolean): Drawable {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_marker_pin)!!.mutate()
        val wrapped = DrawableCompat.wrap(drawable)
        val tint = if (inSeason) category.markerColor().toArgb() else OUT_OF_SEASON_TINT
        DrawableCompat.setTint(wrapped, tint)
        return wrapped
    }
}
