package com.example.buurterij.data

enum class PlantCategory { BERRY, HERB, NUT, FLOWER, MUSHROOM }

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
        PlantType("hazelnoot", "Hazelnoot", "Hazelnut", PlantCategory.NUT, 9, 10),
        PlantType("walnoot", "Walnoot", "Walnut", PlantCategory.NUT, 9, 10),
        PlantType("tamme-kastanje", "Tamme kastanje", "Sweet chestnut", PlantCategory.NUT, 9, 11),
        PlantType("beukennoot", "Beukennoot", "Beechnut", PlantCategory.NUT, 9, 10),
        PlantType("vlierbloesem", "Vlierbloesem", "Elderflower", PlantCategory.FLOWER, 5, 6),
        PlantType("kamille", "Kamille", "Chamomile", PlantCategory.FLOWER, 6, 8),
        PlantType("klaver", "Klaver", "Clover", PlantCategory.FLOWER, 5, 9),
        PlantType("madeliefje", "Madeliefje", "Daisy", PlantCategory.FLOWER, 3, 10),
        PlantType("eekhoorntjesbrood", "Eekhoorntjesbrood", "Porcini", PlantCategory.MUSHROOM, 8, 11),
        PlantType("cantharel", "Cantharel", "Chanterelle", PlantCategory.MUSHROOM, 6, 11),
        PlantType("gewone-zwavelkop", "Honingzwam", "Honey fungus", PlantCategory.MUSHROOM, 9, 11),
        PlantType("parasolzwam", "Parasolzwam", "Parasol mushroom", PlantCategory.MUSHROOM, 7, 10),
    )

    fun byId(id: String): PlantType? = all.find { it.id == id }

    fun groupedByCategory(): Map<PlantCategory, List<PlantType>> = all.groupBy { it.category }
}
