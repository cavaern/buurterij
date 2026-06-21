package com.example.buurterij.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.buurterij.data.PlantCategory

private const val MARKER_TARGET_HEIGHT_PX = 140
private const val OUT_OF_SEASON_OVERLAY_ALPHA = 150

object MarkerIconFactory {
    fun categoryMarkerDrawable(context: Context, category: PlantCategory, inSeason: Boolean): Drawable {
        val source = ContextCompat.getDrawable(context, category.markerDrawableRes())!!
        val aspect = source.intrinsicWidth.toFloat() / source.intrinsicHeight.toFloat()
        val targetWidth = (MARKER_TARGET_HEIGHT_PX * aspect).toInt()
        val scaled = source.toBitmap(width = targetWidth, height = MARKER_TARGET_HEIGHT_PX)

        if (inSeason) {
            return BitmapDrawable(context.resources, scaled)
        }

        val overlaid = scaled.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(overlaid)
        val paint = Paint().apply {
            color = Color.argb(OUT_OF_SEASON_OVERLAY_ALPHA, 128, 128, 128)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }
        canvas.drawRect(0f, 0f, overlaid.width.toFloat(), overlaid.height.toFloat(), paint)
        return BitmapDrawable(context.resources, overlaid)
    }
}
