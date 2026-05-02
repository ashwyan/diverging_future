package com.diverging.futures.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diverging.futures.data.LensType
import com.diverging.futures.data.PlaytestAnalytics
import com.diverging.futures.ui.components.LogoIcon

@Composable
fun CommunityVoiceScreen(onBack: () -> Unit, onSignClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(12.dp))
                LogoIcon(modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Community Voice",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs (from CSS: Votes (836), Signatures (5))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                TabItem(
                    text = "Votes (836)",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                TabItem(
                    text = "Signatures (5)",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(color = Color(0xFFE5E7EB))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                if (selectedTab == 0) {
                    CommunityPreferenceSection()
                } else {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("Signatures List", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next Steps Card (from CSS)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF101828), Color(0xFF1E2939))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Next Steps",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "This data will be submitted to UC Berkeley's outdoor space committee to advocate for community-led design.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onSignClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8FF00)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Add Your Signature",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color(0xFF030213) else Color(0xFF717182)
            )
            if (isSelected) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color(0xFFC8FF00))
                )
            }
        }
    }
}

@Composable
fun CommunityPreferenceSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9FAFB))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon Placeholder
            Box(modifier = Modifier.size(20.dp).background(Color(0xFFC8FF00), CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Community Preference",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0A0A0A)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Site: Shellmound Site • Last updated: Today",
            fontSize = 12.sp,
            color = Color(0xFF4A5565)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        PreferenceBar("What if?", 234, 28, Color(0xFFC8FF00))
        Spacer(modifier = Modifier.height(16.dp))
        PreferenceBar("What could?", 312, 37, Color(0xFF5C8C6E))
        Spacer(modifier = Modifier.height(16.dp))
        PreferenceBar("What is?", 89, 11, Color(0xFFC4785A))
        Spacer(modifier = Modifier.height(16.dp))
        PreferenceBar("What should?", 201, 24, Color(0xFFB8922A))
    }
}

@Composable
fun PreferenceBar(label: String, count: Int, percent: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 14.sp, color = color, fontWeight = FontWeight.Normal)
            Text(text = "$count($percent%)", fontSize = 18.sp, color = Color(0xFF0A0A0A))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFE5E7EB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .fillMaxHeight()
                    .background(color)
            )
        }
    }
}

@Composable
fun PetitionFormScreen(onBack: () -> Unit, onSubmit: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var affiliation by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(12.dp))
                LogoIcon(modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sign Petition",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp) // Responsive padding
        ) {
            Text(
                text = "Support this vision",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0A0A0A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your signature will be collected and submitted alongside community data to stakeholders.",
                fontSize = 14.sp,
                color = Color(0xFF4A5565),
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            PetitionTextField(
                label = "Full Name *", 
                placeholder = "Jane Doe",
                value = name,
                onValueChange = { name = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PetitionTextField(
                label = "Affiliation *", 
                placeholder = "UC Berkeley Student, Resident, etc.",
                value = affiliation,
                onValueChange = { affiliation = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PetitionTextField(
                label = "Email (optional)", 
                placeholder = "jane@example.com",
                value = email,
                onValueChange = { email = it }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (name.isNotBlank() && affiliation.isNotBlank()) {
                        PlaytestAnalytics.logSignature(name, affiliation, email)
                        onSubmit()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8FF00)),
                shape = RoundedCornerShape(33554432.dp),
                enabled = name.isNotBlank() && affiliation.isNotBlank()
            ) {
                Text("Submit Signature", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "By signing, you agree to have your name and affiliation shared with the target recipients of this petition.",
                fontSize = 12.sp,
                color = Color(0xFF6A7282),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // About this petition box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF9FAFB))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "About this petition",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC4785A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Signatures for the Shellmound Site are stored with Diverging and shared with ohlohneshellmound.org and associated advocacy groups.",
                        fontSize = 12.sp,
                        color = Color(0xFF4A5565),
                        lineHeight = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PetitionTextField(
    label: String, 
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0A0A0A))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color(0xFF99A1AF), fontSize = 16.sp) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedBorderColor = Color(0xFFE5E7EB)
            )
        )
    }
}
