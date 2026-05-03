package com.diverging.futures.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.diverging.futures.data.LensType
import com.diverging.futures.data.PlaytestAnalytics
import com.diverging.futures.data.sampleSites
import com.diverging.futures.ui.components.LensTabBar
import com.diverging.futures.ui.components.LogoIcon
import com.google.android.filament.LightManager
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.InstantPlacementPoint
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.ARCameraNode
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Direction
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.node.BillboardNode
import io.github.sceneview.node.LightNode
import io.github.sceneview.rememberEngine

@Composable
fun ArViewContent(
    siteId: String?,
    onNavigateToPetition: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    val bridgeBitmap = remember(context) {
        BitmapFactory.decodeResource(context.resources, com.diverging.futures.R.drawable.bridge_ar)
    }

    val poppyBitmap = remember(context) {
        BitmapFactory.decodeResource(context.resources, com.diverging.futures.R.drawable.poppy_flower_ar)
    }

    val structureBitmap = remember(context) {
        BitmapFactory.decodeResource(context.resources, com.diverging.futures.R.drawable.bridge_ar1)
    }
    
    var currentSite by remember { 
        mutableStateOf(sampleSites.find { it.id == siteId } ?: sampleSites.first()) 
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    var selectedLensType by remember { mutableStateOf(LensType.WHAT_IS) }
    val currentLensData = remember(selectedLensType, currentSite) {
        currentSite.lenses.find { it.type == selectedLensType } ?: currentSite.lenses.first()
    }

    // Voting state
    var votedLenses by remember { mutableStateOf(setOf<LensType>()) }
    val hasVotedCurrent = selectedLensType in votedLenses
    var showVoteRecordedModal by remember { mutableStateOf(false) }

    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine)
    
    // Stable instances
    val cameraPositionProvider = remember(cameraNode) { { cameraNode.worldPosition } }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = constraints.maxWidth.toFloat()
        val screenHeight = constraints.maxHeight.toFloat()
        
        var isGroundDetected by remember { mutableStateOf(false) }
        var groundAnchor by remember { mutableStateOf<Anchor?>(null) }

        if (hasCameraPermission) {
            ARSceneView(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                cameraNode = cameraNode,
                planeRenderer = true,
                sessionConfiguration = { session, config ->
                    // Enable Depth API for better stability on reflective/glossy surfaces
                    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        config.depthMode = Config.DepthMode.AUTOMATIC
                    }
                    // Enable Instant Placement to reduce "drifting" before full plane detection
                    config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                    config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                    
                    // Improve focus and light estimation for outdoor/bright environments
                    config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                    config.focusMode = Config.FocusMode.AUTO
                },
                onSessionUpdated = { session, frame ->
                    if (groundAnchor == null) {
                        // Perform hit test in the center of the screen for more predictable placement
                        val hitResults = frame.hitTest(screenWidth / 2f, screenHeight / 2f)
                        
                        // Prioritize real planes, then fall back to Instant Placement points
                        val hitResult = hitResults.firstOrNull { 
                            val trackable = it.trackable
                            trackable is Plane && trackable.isPoseInPolygon(it.hitPose)
                        } ?: hitResults.firstOrNull { 
                            it.trackable is InstantPlacementPoint 
                        }

                        if (hitResult != null) {
                            groundAnchor = hitResult.createAnchor()
                            isGroundDetected = true
                        }
                    }
                }
            ) {
                LightNode(
                    type = LightManager.Type.DIRECTIONAL,
                    intensity = 100_000f,
                    direction = Direction(0.5f, -1.0f, -0.5f)
                )
                LightNode(
                    type = LightManager.Type.DIRECTIONAL,
                    intensity = 50_000f,
                    direction = Direction(-0.5f, -1.0f, 0.5f)
                )

                groundAnchor?.let { anchor ->
                    AnchorNode(anchor = anchor) {
                        // We keep both nodes in the composition and toggle visibility to prevent
                        // Filament crashes caused by rapid destruction/creation of MaterialInstances.
                        
                        poppyBitmap?.let { bitmap ->
                            BillboardNode(
                                bitmap = bitmap,
                                widthMeters = 4.0f,
                                position = Position(y = 0.75f),
                                cameraPositionProvider = cameraPositionProvider
                            ) {
                                SideEffect {
                                    parentNode.isVisible = selectedLensType == LensType.WHAT_SHOULD
                                }
                            }
                        }

                        bridgeBitmap?.let { bitmap ->
                            BillboardNode(
                                bitmap = bitmap,
                                widthMeters = 6.0f,
                                position = Position(y = 1.0f),
                                cameraPositionProvider = cameraPositionProvider
                            ) {
                                SideEffect {
                                    parentNode.isVisible = selectedLensType == LensType.WHAT_IF
                                }
                            }
                        }

                        structureBitmap?.let { bitmap ->
                            BillboardNode(
                                bitmap = bitmap,
                                widthMeters = 5.0f,
                                position = Position(y = 0.75f),
                                cameraPositionProvider = cameraPositionProvider
                            ) {
                                SideEffect {
                                    parentNode.isVisible = selectedLensType == LensType.WHAT_COULD
                                }
                            }
                        }
                    }
                }
            }

            if (groundAnchor == null && (selectedLensType == LensType.WHAT_IF || selectedLensType == LensType.WHAT_SHOULD || selectedLensType == LensType.WHAT_COULD)) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LogoIcon(modifier = Modifier.size(120.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            if (isGroundDetected) "Placing objects..." else "Point at the ground to scan...",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            // Visual Filter (Triangle/Cone) - Placed here to ensure it's on top of AR and scanning overlays
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    val path = Path().apply {
                        moveTo(width / 2f, height)
                        lineTo(-width * 0.2f, -height * 0.1f)
                        lineTo(width * 1.2f, -height * 0.1f)
                        close()
                    }

                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                selectedLensType.color.copy(alpha = 0.5f),
                                selectedLensType.color.copy(alpha = 0.0f)
                            ),
                            startY = height,
                            endY = 0f
                        )
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission is required for AR", color = Color.White)
            }
        }


        // --- TOP BAR ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.4f))
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    LogoIcon(modifier = Modifier.size(24.dp))
                }
                
                // AR Live Status
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFFC8FF00), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AR live", color = Color.White, fontSize = 12.sp)
                    }
                }
                
                IconButton(
                    onClick = onNavigateToPetition,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Community", tint = Color.White)
                }
            }
        }

        // Lens Type Label (Top Left)
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 64.dp, start = 16.dp)
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = selectedLensType.label,
                color = selectedLensType.color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Site Label (Top Right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 64.dp, end = 16.dp)
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "📍 ${currentSite.name}",
                color = Color.White,
                fontSize = 12.sp
            )
        }

        // --- BOTTOM CONTENT SECTION ---
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            // Lens Description Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f), Color.Black.copy(alpha = 0.7f))
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedLensType.subtitle,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                        
                        // Vote Button
                        Button(
                            onClick = { 
                                votedLenses = votedLenses + selectedLensType
                                PlaytestAnalytics.logVote(currentSite.id, selectedLensType)
                                showVoteRecordedModal = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasVotedCurrent) Color(0xFFC8FF00) else Color(0xFFC8FF00).copy(alpha = 0.8f)
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                if (hasVotedCurrent) "✓ Voted" else "Vote",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "View all 4 lenses to vote",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "${LensType.entries.indexOf(selectedLensType) + 1}/4",
                            color = Color(0xFFC8FF00),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = currentLensData.description,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 23.sp
                    )
                }
            }

            // Lens Tab Bar Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LensType.entries.filter { it != LensType.PETITION }.forEach { type ->
                        val isSelected = type == selectedLensType
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent)
                                .clickable { 
                                    selectedLensType = type
                                    PlaytestAnalytics.logLensSelection(currentSite.id, type)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = type.label,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(6.dp)
                                        .background(type.color)
                                )
                            }
                        }
                    }
                }
            }
            
            // Capture/Action bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(bottom = 32.dp, top = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(32.dp))
                // Big Capture Button
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.width(32.dp))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                }
            }
        }

        // Vote Recorded Modal
        if (showVoteRecordedModal) {
            Dialog(
                onDismissRequest = { showVoteRecordedModal = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✓", fontSize = 36.sp, color = Color(0xFF0A0A0A), textAlign = TextAlign.Center)
                        
                        Text(
                            "Vote Recorded!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0A0A0A),
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            "You voted for ${selectedLensType.label}",
                            fontSize = 14.sp,
                            color = Color(0xFF4A5565),
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            currentLensData.description,
                            fontSize = 12.sp,
                            color = Color(0xFF6A7282),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = { 
                                showVoteRecordedModal = false
                                onNavigateToPetition()
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8FF00)),
                            shape = RoundedCornerShape(33554432.dp)
                        ) {
                            Text("Sign the Petition", color = Color.Black, fontWeight = FontWeight.Medium)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        TextButton(onClick = { showVoteRecordedModal = false }) {
                            Text("Maybe later", color = Color(0xFF6A7282), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
