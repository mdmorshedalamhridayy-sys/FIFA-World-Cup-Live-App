package com.example

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    useCustomControls: Boolean = false
) {
    val context = LocalContext.current
    var isBuffering by remember { mutableStateOf(true) }
    var playbackError by remember { mutableStateOf<String?>(null) }

    // Dynamic Zoom / Regulation States
    var refreshTrigger by remember { mutableStateOf(0) }
    var zoomScale by remember { mutableStateOf(1.0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var resizeMode by remember { mutableStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT) }

    // Control HUD display state
    var showHud by remember { mutableStateOf(true) }

    // Auto-hide controls effect
    LaunchedEffect(showHud) {
        if (showHud) {
            kotlinx.coroutines.delay(5000)
            showHud = false
        }
    }

    // Re-create ExoPlayer when URL or refresh trigger changes
    val exoPlayer = remember(url, refreshTrigger) {
        val playerContext = context

        // Custom live stream LoadControl settings - Completely rules out sluggish buffering and lagging
        val loadControl = androidx.media3.exoplayer.DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                2000,   // minBufferMs (extremely responsive)
                5000,   // maxBufferMs
                800,    // bufferForPlaybackMs
                1200    // bufferForPlaybackAfterRebufferMs
            )
            .build()

        ExoPlayer.Builder(playerContext)
            .setLoadControl(loadControl)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()
            .apply {
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_OFF
                volume = if (isMuted) 0f else 1f

                val mimeType = when {
                    url.contains(".mpd") -> MimeTypes.APPLICATION_MPD
                    url.contains(".m3u8") -> MimeTypes.APPLICATION_M3U8
                    url.contains(".ts") || url.endsWith(".ts") -> MimeTypes.VIDEO_MP2T
                    else -> null
                }

                val mediaItemBuilder = MediaItem.Builder().setUri(url)
                if (mimeType != null) {
                    mediaItemBuilder.setMimeType(mimeType)
                }

                setMediaItem(mediaItemBuilder.build())
                prepare()
            }
    }

    // React to continuous mute/unmute updates
    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    // Register Player Event Listener
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
                if (playbackState == Player.STATE_READY) {
                    playbackError = null
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                playbackError = when (error.errorCode) {
                    PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                        exoPlayer.seekToDefaultPosition()
                        exoPlayer.prepare()
                        "Live window updated, seeking back to current feed..."
                    }
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                        "Network connection failed. Attempting auto reconnection..."
                    }
                    else -> {
                        "Streaming feed offline. Click Refresh to reload stream."
                    }
                }
                
                // Auto reconnect system to ensure uninterrupted, smooth gameplay
                exoPlayer.prepare()
                exoPlayer.play()
                isBuffering = false
            }
        }

        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .clipToBounds()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showHud = !showHud
            }
    ) {
        // Video render view with pinch-to-zoom and pan support
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        zoomScale = (zoomScale * zoom).coerceIn(1.0f, 3.0f)
                        if (zoomScale > 1.0f) {
                            offset = Offset(offset.x + pan.x, offset.y + pan.y)
                        } else {
                            offset = Offset.Zero
                        }
                    }
                }
                .graphicsLayer {
                    scaleX = zoomScale
                    scaleY = zoomScale
                    translationX = offset.x
                    translationY = offset.y
                }
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = !useCustomControls
                        resizeMode = resizeMode
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { playerView ->
                    playerView.player = exoPlayer
                    playerView.resizeMode = resizeMode
                }
            )
        }

        // Overlay for Buffering
        if (isBuffering && playbackError == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(52.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        }

        // Overlay for Playback Errors
        if (playbackError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = playbackError ?: "Error Loading Stream",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = {
                            playbackError = null
                            isBuffering = true
                            refreshTrigger++
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Retry")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Retry Stream Connection", color = Color.Black)
                    }
                }
            }
        }

        // Advanced Regulation and Zoom Controls HUD Overlay
        AnimatedVisibility(
            visible = showHud,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xDD211F26)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF49454F).copy(alpha = 0.8f)),
                modifier = Modifier.padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Zoom Out [-]
                    IconButton(
                        onClick = {
                            zoomScale = (zoomScale - 0.2f).coerceIn(1.0f, 3.0f)
                            if (zoomScale <= 1.0f) offset = Offset.Zero
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                    ) {
                        Text(
                            text = "−",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Zoom Label & Manual Reset
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                zoomScale = 1.0f
                                offset = Offset.Zero
                            }
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Zoom: ${"%.1f".format(zoomScale)}x",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        if (zoomScale > 1.0f) {
                            Text(
                                text = " (Reset)",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }

                    // Zoom In [+]
                    IconButton(
                        onClick = {
                            zoomScale = (zoomScale + 0.2f).coerceIn(1.0f, 3.0f)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                    ) {
                        Text(
                            text = "+",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Separator line
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(Color.White.copy(alpha = 0.2f))
                    )

                    // Aspect Ratio switcher (Regulation System)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                resizeMode = when (resizeMode) {
                                    AspectRatioFrameLayout.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                    AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                                    else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                                }
                            }
                            .background(Color.White.copy(alpha = 0.08f))
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .border(1.5.dp, Color.White, RoundedCornerShape(2.dp))
                        )
                        Text(
                            text = when (resizeMode) {
                                AspectRatioFrameLayout.RESIZE_MODE_FIT -> "Original Fit"
                                AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> "Crop Zoom"
                                else -> "Stretch Fill"
                            },
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Separate reload stream (Auto Refresh)
                    IconButton(
                        onClick = {
                            playbackError = null
                            isBuffering = true
                            refreshTrigger++
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape)
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reload stream",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
