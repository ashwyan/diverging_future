package com.diverging.futures.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diverging.futures.data.LensType

@Composable
fun LensTabBar(
    selectedLens: LensType,
    onLensSelected: (LensType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LensType.values().forEach { lensType ->
            val isSelected = lensType == selectedLens
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) lensType.color else Color.Transparent)
                    .clickable { onLensSelected(lensType) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (lensType) {
                        LensType.WHAT_IS -> "is"
                        LensType.WHAT_SHOULD -> "sh"
                        LensType.WHAT_COULD -> "co"
                        LensType.WHAT_IF -> "if"
                        LensType.PETITION -> "P"
                    },
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
