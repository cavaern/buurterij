package com.example.buurterij.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlantTypeTest {
    private val summerOnly = PlantType("x", "x", "x", PlantCategory.BERRY, 6, 8)
    private val wraparound = PlantType("y", "y", "y", PlantCategory.HERB, 11, 2)

    @Test
    fun `normal range - month inside is in season`() {
        assertTrue(summerOnly.isInSeason(7))
    }

    @Test
    fun `normal range - month outside is not in season`() {
        assertFalse(summerOnly.isInSeason(10))
    }

    @Test
    fun `wraparound range - december is in season`() {
        assertTrue(wraparound.isInSeason(12))
    }

    @Test
    fun `wraparound range - january is in season`() {
        assertTrue(wraparound.isInSeason(1))
    }

    @Test
    fun `wraparound range - july is not in season`() {
        assertFalse(wraparound.isInSeason(7))
    }

    @Test
    fun `boundary months are inclusive on both ends`() {
        assertTrue(summerOnly.isInSeason(6))
        assertTrue(summerOnly.isInSeason(8))
        assertTrue(wraparound.isInSeason(11))
        assertTrue(wraparound.isInSeason(2))
    }
}
