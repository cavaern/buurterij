package com.example.buurterij.data

enum class PlantCategory { BERRY, HERB, NUT, FLOWER, MUSHROOM, SEED, MARKET_STALL }

enum class Language(val label: String) {
    DUTCH("Dutch"),
    ENGLISH("English"),
    GERMAN("German"),
    FRENCH("French"),
    LATIN("Latin"),
}

data class PlantType(
    val id: String,
    val dutchName: String,
    val englishName: String,
    val category: PlantCategory,
    val seasonStartMonth: Int,
    val seasonEndMonth: Int,
    val germanName: String = "",
    val frenchName: String = "",
    val latinName: String = "",
)

fun PlantType.isInSeason(monthNow: Int): Boolean =
    if (seasonStartMonth <= seasonEndMonth) {
        monthNow in seasonStartMonth..seasonEndMonth
    } else {
        monthNow >= seasonStartMonth || monthNow <= seasonEndMonth
    }

fun PlantType.nameFor(language: Language): String = when (language) {
    Language.DUTCH -> dutchName
    Language.ENGLISH -> englishName
    Language.GERMAN -> germanName
    Language.FRENCH -> frenchName
    Language.LATIN -> latinName
}

/**
 * Resolves the name(s) to show for this plant type given the user's chosen main and
 * optional secondary display languages, e.g. "Brombeere (Braam)" or just "Braam" when
 * [secondary] is null.
 */
fun PlantType.displayName(main: Language, secondary: Language? = null): String {
    val mainName = nameFor(main).ifBlank { dutchName }
    val secondaryName = secondary?.let { nameFor(it) }?.takeIf { it.isNotBlank() && it != mainName }
    return if (secondaryName != null) "$mainName ($secondaryName)" else mainName
}

object PlantCatalog {
    val all: List<PlantType> = listOf(
        PlantType("braam", "Braam", "Blackberry", PlantCategory.BERRY, 8, 9, "Brombeere", "Mûre", "Rubus fruticosus"),
        PlantType("vlier", "Vlier", "Elderberry", PlantCategory.BERRY, 8, 9, "Holunder", "Sureau noir", "Sambucus nigra"),
        PlantType("sleedoorn", "Sleedoorn", "Blackthorn (sloe)", PlantCategory.BERRY, 10, 12, "Schlehe", "Prunellier", "Prunus spinosa"),
        PlantType("lijsterbes", "Lijsterbes", "Rowan", PlantCategory.BERRY, 8, 10, "Eberesche", "Sorbier des oiseleurs", "Sorbus aucuparia"),
        PlantType("duindoorn", "Duindoorn", "Sea buckthorn", PlantCategory.BERRY, 9, 11, "Sanddorn", "Argousier", "Hippophae rhamnoides"),
        PlantType("hondsroos", "Hondsroos", "Dog rose (rosehip)", PlantCategory.BERRY, 9, 11, "Hundsrose", "Églantier", "Rosa canina"),
        PlantType("bosbes", "Bosbes", "Bilberry", PlantCategory.BERRY, 7, 8, "Heidelbeere", "Myrtille", "Vaccinium myrtillus"),
        PlantType("meidoorn", "Meidoorn", "Hawthorn", PlantCategory.BERRY, 9, 11, "Weißdorn", "Aubépine", "Crataegus monogyna"),
        PlantType("brandnetel", "Brandnetel", "Stinging nettle", PlantCategory.HERB, 3, 6, "Brennnessel", "Ortie dioïque", "Urtica dioica"),
        PlantType("daslook", "Daslook", "Wild garlic", PlantCategory.HERB, 3, 5, "Bärlauch", "Ail des ours", "Allium ursinum"),
        PlantType("paardenbloem", "Paardenbloem", "Dandelion", PlantCategory.HERB, 4, 9, "Löwenzahn", "Pissenlit", "Taraxacum officinale"),
        PlantType("zevenblad", "Zevenblad", "Ground elder", PlantCategory.HERB, 4, 7, "Giersch", "Égopode podagraire", "Aegopodium podagraria"),
        PlantType("veldzuring", "Veldzuring", "Sorrel", PlantCategory.HERB, 4, 8, "Wiesen-Sauerampfer", "Oseille des prés", "Rumex acetosa"),
        PlantType("look-zonder-look", "Look-zonder-look", "Garlic mustard", PlantCategory.HERB, 3, 6, "Knoblauchsrauke", "Alliaire officinale", "Alliaria petiolata"),
        PlantType("wilde-kervel", "Wilde kervel", "Wild chervil", PlantCategory.HERB, 4, 6, "Wiesenkerbel", "Cerfeuil sauvage", "Anthriscus sylvestris"),
        PlantType("hazelnoot", "Hazelnoot", "Hazelnut", PlantCategory.NUT, 9, 10, "Haselnuss", "Noisette", "Corylus avellana"),
        PlantType("walnoot", "Walnoot", "Walnut", PlantCategory.NUT, 9, 10, "Walnuss", "Noix", "Juglans regia"),
        PlantType("tamme-kastanje", "Tamme kastanje", "Sweet chestnut", PlantCategory.NUT, 9, 11, "Esskastanie", "Châtaigne", "Castanea sativa"),
        PlantType("beukennoot", "Beukennoot", "Beechnut", PlantCategory.NUT, 9, 10, "Buchecker", "Faîne", "Fagus sylvatica"),
        PlantType("vlierbloesem", "Vlierbloesem", "Elderflower", PlantCategory.FLOWER, 5, 6, "Holunderblüte", "Fleur de sureau", "Sambucus nigra"),
        PlantType("kamille", "Kamille", "Chamomile", PlantCategory.FLOWER, 6, 8, "Kamille", "Camomille", "Matricaria chamomilla"),
        PlantType("klaver", "Klaver", "Clover", PlantCategory.FLOWER, 5, 9, "Klee", "Trèfle", "Trifolium repens"),
        PlantType("madeliefje", "Madeliefje", "Daisy", PlantCategory.FLOWER, 3, 10, "Gänseblümchen", "Pâquerette", "Bellis perennis"),
        PlantType("eekhoorntjesbrood", "Eekhoorntjesbrood", "Porcini", PlantCategory.MUSHROOM, 8, 11, "Steinpilz", "Cèpe de Bordeaux", "Boletus edulis"),
        PlantType("cantharel", "Cantharel", "Chanterelle", PlantCategory.MUSHROOM, 6, 11, "Pfifferling", "Girolle", "Cantharellus cibarius"),
        PlantType("gewone-zwavelkop", "Honingzwam", "Honey fungus", PlantCategory.MUSHROOM, 9, 11, "Hallimasch", "Armillaire couleur de miel", "Armillaria mellea"),
        PlantType("parasolzwam", "Parasolzwam", "Parasol mushroom", PlantCategory.MUSHROOM, 7, 10, "Parasolpilz", "Coulemelle", "Macrolepiota procera"),
        PlantType("marktkraam", "Marktkraam", "Market stall", PlantCategory.MARKET_STALL, 1, 12, "Marktstand", "Étal de marché", "—"),
        PlantType("klaproos", "Klaproos", "Poppy", PlantCategory.SEED, 7, 9, "Klatschmohn", "Coquelicot", "Papaver rhoeas"),
        PlantType("zonnebloem", "Zonnebloem", "Sunflower", PlantCategory.SEED, 8, 10, "Sonnenblume", "Tournesol", "Helianthus annuus"),
        PlantType("framboos", "Framboos", "Raspberry (cuttings)", PlantCategory.SEED, 11, 2, "Himbeere", "Framboise", "Rubus idaeus"),
    )

    fun byId(id: String): PlantType? = all.find { it.id == id }

    fun groupedByCategory(): Map<PlantCategory, List<PlantType>> = all.groupBy { it.category }
}
