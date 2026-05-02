package com.diverging.futures.data

import androidx.compose.ui.graphics.Color

enum class LensType(val label: String, val color: Color, val subtitle: String) {
    WHAT_IS("What is?", Color(0xFFC4785A), "Present condition"),
    WHAT_SHOULD("What should?", Color(0xFFB8922A), "Desired, achievable"),
    WHAT_COULD("What could?", Color(0xFF5C8C6E), "Imaginative path"),
    WHAT_IF("What if?", Color(0xFF6B8E00), "Radical reimagining"),
    PETITION("Petition", Color(0xFFC4785A), "Community Voice")
}

data class Site(
    val id: String,
    val name: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val lenses: List<LensData>,
    val isComingSoon: Boolean = false
)

data class LensData(
    val type: LensType,
    val title: String,
    val description: String,
    val imageResId: Int? = null
)

val sampleSites = listOf(
    Site(
        id = "faculty_glade",
        name = "Faculty Glade",
        location = "UC Berkeley Campus",
        latitude = 37.8719,
        longitude = -122.2585,
        lenses = listOf(
            LensData(
                LensType.WHAT_IS, 
                "Shellmound Site", 
                "The Shellmound remains largely invisible in the built environment. It is a sacred Ohlone burial ground buried under the campus.",
                null
            ),
            LensData(
                LensType.WHAT_SHOULD, 
                "Rewilding", 
                "Indigenous plants rewild the glade, restoring the native ecosystem and Ohlone stewardship.",
                null
            ),
            LensData(
                LensType.WHAT_COULD, 
                "Imaginative negotiation", 
                "An Indigenous artist-made structure or reflection space that prompts dialogue about the site's history.",
                null
            ),
            LensData(
                LensType.WHAT_IF, 
                "Radical reimagining", 
                "The land is rematriated. It has returned to Ohlone care and has been transformed into a site of ceremony and community.",
                null
            )
        )
    ),
    Site(
        id = "sproul_plaza",
        name = "Sproul Plaza",
        location = "UC Berkeley Campus",
        latitude = 37.8696,
        longitude = -122.2591,
        lenses = emptyList(),
        isComingSoon = true
    ),
    Site(
        id = "peoples_park",
        name = "People's Park",
        location = "Berkeley, CA",
        latitude = 37.8661,
        longitude = -122.2587,
        lenses = emptyList(),
        isComingSoon = true
    )
)
