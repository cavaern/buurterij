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
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.ui.theme.DimmedGrey

private const val MARKER_TARGET_HEIGHT_PX = 140
private const val OUT_OF_SEASON_OVERLAY_ALPHA = 150
private const val CLUSTER_DIAMETER_PX = 130
private const val DIMMED_DOT_DIAMETER_PX = 18

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

    fun clusterMarkerDrawable(context: Context, count: Int, color: androidx.compose.ui.graphics.Color): Drawable {
        val bitmap = Bitmap.createBitmap(CLUSTER_DIAMETER_PX, CLUSTER_DIAMETER_PX, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val radius = CLUSTER_DIAMETER_PX / 2f

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color.toArgb() }
        canvas.drawCircle(radius, radius, radius, circlePaint)

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawCircle(radius, radius, radius - 2f, borderPaint)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.WHITE
            textSize = CLUSTER_DIAMETER_PX * 0.4f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        val text = count.toString()
        val textBounds = android.graphics.Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textY = radius - textBounds.exactCenterY()
        canvas.drawText(text, radius, textY, textPaint)

        return BitmapDrawable(context.resources, bitmap)
    }

    fun dimmedDotDrawable(context: Context): Drawable {
        val bitmap = Bitmap.createBitmap(DIMMED_DOT_DIAMETER_PX, DIMMED_DOT_DIAMETER_PX, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val radius = DIMMED_DOT_DIAMETER_PX / 2f
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = DimmedGrey.toArgb() }
        canvas.drawCircle(radius, radius, radius, paint)
        return BitmapDrawable(context.resources, bitmap)
    }
}
