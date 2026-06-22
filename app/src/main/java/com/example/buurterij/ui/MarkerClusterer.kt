package com.example.buurterij.ui

import com.example.buurterij.data.PlantCategory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import android.graphics.Point

sealed class MapCluster {
    abstract val position: GeoPoint

    data class Single(val spot: SpotUiModel, override val position: GeoPoint) : MapCluster()

    data class Group(
        val spots: List<SpotUiModel>,
        override val position: GeoPoint,
        val categories: Set<PlantCategory>,
    ) : MapCluster()
}

object MarkerClusterer {
    private const val DEFAULT_PIXEL_THRESHOLD = 110.0

    fun cluster(
        spots: List<SpotUiModel>,
        projection: Projection,
        pixelThreshold: Double = DEFAULT_PIXEL_THRESHOLD,
    ): List<MapCluster> {
        if (spots.isEmpty()) return emptyList()

        val screenPoints = spots.map { spot ->
            projection.toPixels(GeoPoint(spot.latitude, spot.longitude), Point())
        }
        val thresholdSq = pixelThreshold * pixelThreshold
        val visited = BooleanArray(spots.size)
        val clusters = mutableListOf<MapCluster>()

        for (seedIndex in spots.indices) {
            if (visited[seedIndex]) continue
            val groupIndices = mutableListOf(seedIndex)
            visited[seedIndex] = true

            var frontier = 0
            while (frontier < groupIndices.size) {
                val current = screenPoints[groupIndices[frontier]]
                for (candidate in spots.indices) {
                    if (visited[candidate]) continue
                    val other = screenPoints[candidate]
                    val dx = (current.x - other.x).toDouble()
                    val dy = (current.y - other.y).toDouble()
                    if (dx * dx + dy * dy <= thresholdSq) {
                        visited[candidate] = true
                        groupIndices.add(candidate)
                    }
                }
                frontier++
            }

            if (groupIndices.size == 1) {
                val spot = spots[seedIndex]
                clusters.add(MapCluster.Single(spot, GeoPoint(spot.latitude, spot.longitude)))
            } else {
                val members = groupIndices.map { spots[it] }
                val centroidLat = members.sumOf { it.latitude } / members.size
                val centroidLon = members.sumOf { it.longitude } / members.size
                clusters.add(
                    MapCluster.Group(
                        spots = members,
                        position = GeoPoint(centroidLat, centroidLon),
                        categories = members.map { it.plantType.category }.toSet(),
                    ),
                )
            }
        }

        return clusters
    }
}
