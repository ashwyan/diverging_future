package com.diverging.futures.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayer(videoResId: Int, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                setVideoURI(uri)
                setOnPreparedListener { it.isLooping = true }
                start()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
