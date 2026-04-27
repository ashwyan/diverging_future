package com.diverging.futures.data

import androidx.compose.ui.graphics.Color

enum class LensType(val label: String, val color: Color) {
    WHAT_IS("What is?", Color(0xFFD17F64)),
    WHAT_SHOULD("What should?", Color(0xFFC2A13D)),
    WHAT_COULD("What could?", Color(0xFF5E947A)),
    WHAT_IF("What if?", Color(0xFFA8D900)),
    PETITION("Petition", Color(0xFFFF5722))
}

data class Site(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val lenses: List<LensData>
)

data class LensData(
    val type: LensType,
    val title: String,
    val description: String,
    val imageResId: Int? = null
)

val sampleSites = listOf(
    Site(
        id = "uc_berkeley",
        name = "UC Berkeley Campus",
        latitude = 37.8719,
        longitude = -122.2585,
        lenses = listOf(
            LensData(
                LensType.WHAT_IS, 
                "Shellmound", 
                "This was once a vibrant Ohlone village and sacred site. Today, it's buried under asphalt and commerce.",
                null
            ),
            LensData(
                LensType.WHAT_SHOULD, 
                "Rematriation", 
                "Returning the land to the Sogorea Te’ Land Trust to restore indigenous stewardship and ecological balance.",
                null
            ),
            LensData(
                LensType.WHAT_COULD, 
                "Urban Commons", 
                "A community-managed green space where food, energy, and knowledge are shared freely among all residents.",
                null
            ),
            LensData(
                LensType.WHAT_IF, 
                "Decolonized Future", 
                "A radical world where the structures of settler-colonialism have been dismantled, replaced by ancestral wisdom.",
                null // Placeholder, will be linked in UI layer
            ),
            LensData(
                LensType.PETITION, 
                "Sign the Petition", 
                "Support the Sogorea Te’ Land Trust in their mission to return land to indigenous hands.",
                null
            )
        )
    )
)
