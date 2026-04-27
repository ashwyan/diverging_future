package com.diverging.futures.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.diverging.futures.data.LensType
import com.diverging.futures.data.PlaytestAnalytics
import com.diverging.futures.data.sampleSites
import com.diverging.futures.ui.components.LensOverlayView
import com.diverging.futures.ui.components.LensTabBar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import androidx.compose.ui.tooling.preview.Preview as ComposePreview

@Composable
fun ArViewContent(
    siteId: String?,
    onNavigateToPetition: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    var currentSite by remember { 
        mutableStateOf(sampleSites.find { it.id == siteId } ?: sampleSites.first()) 
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var isUserAtSite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasLocationPermission) {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                location?.let {
                    val results = FloatArray(1)
                    android.location.Location.distanceBetween(
                        it.latitude, it.longitude,
                        currentSite.latitude, currentSite.longitude,
                        results
                    )
                    isUserAtSite = results[0] < 500
                }
            }
        }
    }
    
    LaunchedEffect(currentSite.id) {
        PlaytestAnalytics.logLensSelection(currentSite.id, LensType.WHAT_IS)
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
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
        val data = currentSite.lenses.find { it.type == selectedLensType } ?: currentSite.lenses.first()
        if (selectedLensType == LensType.WHAT_IF) {
            val bridgeResId = context.resources.getIdentifier("bridge_ar", "drawable", context.packageName)
            if (bridgeResId != 0) {
                data.copy(imageResId = bridgeResId)
            } else data
        } else data
    }

    Surface(color = Color.Black) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (hasCameraPermission) {
                var isGroundDetected by remember { mutableStateOf(false) }
                
                ARScene(
                    modifier = Modifier.fillMaxSize(),
                    planeRenderer = true,
                    onSessionUpdated = { _, frame ->
                        if (!isGroundDetected) {
                            val planes = frame.getUpdatedPlanes()
                            if (planes.any { it.trackingState == TrackingState.TRACKING }) {
                                isGroundDetected = true
                            }
                        }
                    }
                )

                if (!isGroundDetected && selectedLensType == LensType.WHAT_IF) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Scanning for ground...",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(16.dp)
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

            if (isUserAtSite || siteId != null) {
                LensOverlayView(lensData = currentLensData)
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Current Site: ${currentSite.name}",
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelMedium
                )
                LensTabBar(
                    selectedLens = selectedLensType,
                    onLensSelected = { lensType ->
                        if (lensType == LensType.PETITION) {
                            onNavigateToPetition()
                        } else {
                            selectedLensType = lensType
                            PlaytestAnalytics.logLensSelection(currentSite.id, lensType)
                        }
                    }
                )
            }
        }
    }
}
