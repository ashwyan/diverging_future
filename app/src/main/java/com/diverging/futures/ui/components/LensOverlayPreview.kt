package com.diverging.futures.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diverging.futures.data.LensData
import com.diverging.futures.data.LensType

@Preview(showBackground = true)
@Composable
fun LensOverlayWithBridgePreview() {
    // Manually construct a LensData with a fixed resource ID for testing
    // This simulates the What If lens with the bridge image
    val testLensData = LensData(
        type = LensType.WHAT_IF,
        title = "Decolonized Future",
        description = "A radical world where the structures of settler-colonialism have been dismantled, replaced by ancestral wisdom.",
        imageResId = com.diverging.futures.R.drawable.bridge_ar
    )
    LensOverlayView(lensData = testLensData)
}
