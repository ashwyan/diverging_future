package com.diverging.futures.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.diverging.futures.data.LensType

import com.diverging.futures.data.PlaytestAnalytics

@Composable
fun PetitionScreen(onBack: () -> Unit) {
    var hasVoted by remember { mutableStateOf(false) }
    var selectedLens by remember { mutableStateOf<LensType?>(null) }
    val context = LocalContext.current

    val communityVotes = remember {
        mapOf(
            LensType.WHAT_SHOULD to Pair(55, 44),
            LensType.WHAT_IF to Pair(34, 27),
            LensType.WHAT_IS to Pair(25, 20),
            LensType.WHAT_COULD to Pair(12, 10)
        )
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!hasVoted) {
                VoteScreen(
                    selectedLens = selectedLens,
                    onLensSelected = { selectedLens = it },
                    onSubmit = { 
                        if (selectedLens != null) {
                            PlaytestAnalytics.logVote(selectedLens!!)
                            hasVoted = true 
                        }
                    }
                )
            } else {
                ResultsScreen(
                    communityVotes = communityVotes,
                    onPetitionClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sogoreate-landtrust.org"))
                        context.startActivity(intent)
                    }
                )
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetitionScreenVotePreview() {
    PetitionScreen(onBack = {})
}

@Preview(showBackground = true)
@Composable
fun PetitionScreenResultsPreview() {
    val communityVotes = mapOf(
        LensType.WHAT_SHOULD to Pair(55, 44),
        LensType.WHAT_IF to Pair(34, 27),
        LensType.WHAT_IS to Pair(25, 20),
        LensType.WHAT_COULD to Pair(12, 10)
    )
    ResultsScreen(communityVotes = communityVotes, onPetitionClick = {})
}

@Composable
fun VoteScreen(
    selectedLens: LensType?,
    onLensSelected: (LensType) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cast Your Vote",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Which future do you want to see realized?",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        val options = listOf(
            LensType.WHAT_IF,
            LensType.WHAT_SHOULD,
            LensType.WHAT_COULD,
            LensType.WHAT_IS
        )

        options.forEach { lensType ->
            VoteOption(
                lensType = lensType,
                isSelected = selectedLens == lensType,
                onClick = { onLensSelected(lensType) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSubmit,
            enabled = selectedLens != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("SUBMIT VOTE", modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun VoteOption(
    lensType: LensType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) lensType.color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, lensType.color)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
        }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(selectedColor = lensType.color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = lensType.label,
                fontSize = 18.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ResultsScreen(
    communityVotes: Map<LensType, Pair<Int, Int>>,
    onPetitionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Community Results",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        communityVotes.forEach { (lensType, data) ->
            val (count, pct) = data
            ResultBar(lensType = lensType, count = count, percentage = pct)
            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onPetitionClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LensType.PETITION.color)
        ) {
            Text("SIGN THE PETITION", modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun ResultBar(lensType: LensType, count: Int, percentage: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = lensType.label, fontWeight = FontWeight.Bold)
            Text(text = "$count ($percentage%)", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .fillMaxHeight()
                    .background(lensType.color, RoundedCornerShape(12.dp))
            )
        }
    }
}
