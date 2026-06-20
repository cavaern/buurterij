package com.example.buurterij.data

enum class PlantCategory { BERRY, HERB }

data class PlantType(
    val id: String,
    val dutchName: String,
    val englishName: String,
    val category: PlantCategory,
    val seasonStartMonth: Int,
    val seasonEndMonth: Int,
)

fun PlantType.isInSeason(monthNow: Int): Boolean =
    if (seasonStartMonth <= seasonEndMonth) {
        monthNow in seasonStartMonth..seasonEndMonth
    } else {
        monthNow >= seasonStartMonth || monthNow <= seasonEndMonth
    }

object PlantCatalog {
    val all: List<PlantType> = listOf(
        PlantType("braam", "Braam", "Blackberry", PlantCategory.BERRY, 8, 9),
        PlantType("vlier", "Vlier", "Elderberry", PlantCategory.BERRY, 8, 9),
        PlantType("sleedoorn", "Sleedoorn", "Blackthorn (sloe)", PlantCategory.BERRY, 10, 12),
        PlantType("lijsterbes", "Lijsterbes", "Rowan", PlantCategory.BERRY, 8, 10),
        PlantType("duindoorn", "Duindoorn", "Sea buckthorn", PlantCategory.BERRY, 9, 11),
        PlantType("hondsroos", "Hondsroos", "Dog rose (rosehip)", PlantCategory.BERRY, 9, 11),
        PlantType("bosbes", "Bosbes", "Bilberry", PlantCategory.BERRY, 7, 8),
        PlantType("meidoorn", "Meidoorn", "Hawthorn", PlantCategory.BERRY, 9, 11),
        PlantType("brandnetel", "Brandnetel", "Stinging nettle", PlantCategory.HERB, 3, 6),
        PlantType("daslook", "Daslook", "Wild garlic", PlantCategory.HERB, 3, 5),
        PlantType("paardenbloem", "Paardenbloem", "Dandelion", PlantCategory.HERB, 4, 9),
        PlantType("zevenblad", "Zevenblad", "Ground elder", PlantCategory.HERB, 4, 7),
        PlantType("veldzuring", "Veldzuring", "Sorrel", PlantCategory.HERB, 4, 8),
        PlantType("look-zonder-look", "Look-zonder-look", "Garlic mustard", PlantCategory.HERB, 3, 6),
        PlantType("wilde-kervel", "Wilde kervel", "Wild chervil", PlantCategory.HERB, 4, 6),
    )

    fun byId(id: String): PlantType? = all.find { it.id == id }

    fun groupedByCategory(): Map<PlantCategory, List<PlantType>> = all.groupBy { it.category }
}
