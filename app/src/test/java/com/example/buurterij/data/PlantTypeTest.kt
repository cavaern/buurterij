package com.example.buurterij.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlantTypeTest {
    private val summerOnly = PlantType("x", "x", "x", PlantCategory.BERRY, 6, 8)
    private val wraparound = PlantType("y", "y", "y", PlantCategory.HERB, 11, 2)

    private val braam = PlantType(
        id = "braam",
        dutchName = "Braam",
        englishName = "Blackberry",
        category = PlantCategory.BERRY,
        seasonStartMonth = 8,
        seasonEndMonth = 9,
        germanName = "Brombeere",
        frenchName = "Mûre",
        latinName = "Rubus fruticosus",
    )

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

    @Test
    fun `displayName with only main language returns just that name`() {
        assertEquals("Braam", braam.displayName(Language.DUTCH))
    }

    @Test
    fun `displayName with main and secondary combines both`() {
        assertEquals("Brombeere (Braam)", braam.displayName(Language.GERMAN, Language.DUTCH))
    }

    @Test
    fun `displayName with null secondary returns just main name`() {
        assertEquals("Rubus fruticosus", braam.displayName(Language.LATIN, null))
    }

    @Test
    fun `displayName ignores secondary when it equals main name`() {
        assertEquals("Braam", braam.displayName(Language.DUTCH, Language.DUTCH))
    }

    @Test
    fun `nameFor returns correct name for each language`() {
        assertEquals("Braam", braam.nameFor(Language.DUTCH))
        assertEquals("Blackberry", braam.nameFor(Language.ENGLISH))
        assertEquals("Brombeere", braam.nameFor(Language.GERMAN))
        assertEquals("Mûre", braam.nameFor(Language.FRENCH))
        assertEquals("Rubus fruticosus", braam.nameFor(Language.LATIN))
    }
}
