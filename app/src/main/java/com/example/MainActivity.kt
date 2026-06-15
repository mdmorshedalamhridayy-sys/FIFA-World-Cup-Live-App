package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    TvAppHomeScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun getAttributionTag(): String? {
        return "default"
    }
}

@Composable
fun TvAppHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TvAppViewModel = viewModel()
) {
    val context = LocalContext.current
    val selectedChannel by viewModel.selectedChannel.collectAsState()
    val filteredChannels by viewModel.filteredChannels.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()

    // Creator Info Dialog State
    var showInfoDialog by remember { mutableStateOf(false) }

    // Query state
    var searchActive by remember { mutableStateOf(false) }

    // Screen layout responsive measurements
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp > 600

    if (showInfoDialog) {
        CreatorInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Box(
        modifier = modifier
            .background(CharcoalBg)
    ) {
        // Aesthetic stadium soccer pitch line replaced/updated with high fidelity "Geometric Balance" layout lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 60.dp.toPx()
            // Draw clean vertical grid lines
            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = SlateBorder.copy(alpha = 0.08f),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                x += gridSpacing
            }
            // Draw clean horizontal grid lines
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = SlateBorder.copy(alpha = 0.08f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
                y += gridSpacing
            }
        }

        if (isWideScreen) {
            // WIDE SCREEN / TABLET LAYOUT: SIDE-BY-SIDE PANELS
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Player & Active Details (Takes 55% space)
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TvHeaderSection(onInfoClick = { showInfoDialog = true })

                    selectedChannel?.let { channel ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.2.dp, SlateBorder, RoundedCornerShape(24.dp))
                        ) {
                            VideoPlayer(
                                url = channel.url,
                                isMuted = isMuted,
                                modifier = Modifier.fillMaxSize()
                            )
                            LiveBadgeOverlay()
                        }

                        ActiveChannelDetails(
                            channel = channel,
                            isMuted = isMuted,
                            isFavorite = favoriteIds.contains(channel.id),
                            onMuteToggle = { viewModel.toggleMute() },
                            onFavoriteToggle = { viewModel.toggleFavorite(channel.id) },
                            onShareClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "FIFA Live Stream: ${channel.name}")
                                    putExtra(Intent.EXTRA_TEXT, "Watch ${channel.name} live with smooth streaming: ${channel.url}")
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Channel"))
                            }
                        )
                    } ?: LoadingStatePlaceholder()
                }

                // Right Column: Search + Category Chips + Channels list
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(SlateCardBg.copy(alpha = 0.8f), RoundedCornerShape(24.dp))
                        .border(1.dp, SlateBorder, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SearchAndHeader(
                        query = searchQuery,
                        onQueryChange = { viewModel.searchQuery.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    CategoryChipsRow(
                        selectedCategory = selectedCategory,
                        favoriteCount = favoriteIds.size,
                        onCategorySelect = { viewModel.selectedCategory.value = it }
                    )

                    ChannelsSection(
                        channels = filteredChannels,
                        selectedChannel = selectedChannel,
                        favoriteIds = favoriteIds,
                        onChannelClick = { viewModel.selectChannel(it) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it.id) },
                        isGrid = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            // COMPACT MOBILE PHONE LAYOUT: STACKED PANEL
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Header section
                TvHeaderSection(
                    onInfoClick = { showInfoDialog = true },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                // Continuous sticky player at the top
                selectedChannel?.let { channel ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .background(Color.Black)
                            .border(1.dp, SlateBorder)
                    ) {
                        VideoPlayer(
                            url = channel.url,
                            isMuted = isMuted,
                            modifier = Modifier.fillMaxSize()
                        )
                        LiveBadgeOverlay()
                    }

                    ActiveChannelDetails(
                        channel = channel,
                        isMuted = isMuted,
                        isFavorite = favoriteIds.contains(channel.id),
                        onMuteToggle = { viewModel.toggleMute() },
                        onFavoriteToggle = { viewModel.toggleFavorite(channel.id) },
                        onShareClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "FIFA Live Stream: ${channel.name}")
                                putExtra(Intent.EXTRA_TEXT, "Watch ${channel.name} live with smooth streaming: ${channel.url}")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Channel"))
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } ?: LoadingStatePlaceholder(modifier = Modifier.padding(16.dp))

                // Bottom Content: Search, Filter, Channels (Grid format on mobile is super spacious)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(SlateCardBg, CharcoalBg)
                            ),
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(SlateBorder, Color.Transparent)
                            ),
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                ) {
                    SearchAndHeader(
                        query = searchQuery,
                        onQueryChange = { viewModel.searchQuery.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CategoryChipsRow(
                        selectedCategory = selectedCategory,
                        favoriteCount = favoriteIds.size,
                        onCategorySelect = { viewModel.selectedCategory.value = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ChannelsSection(
                        channels = filteredChannels,
                        selectedChannel = selectedChannel,
                        favoriteIds = favoriteIds,
                        onChannelClick = { viewModel.selectChannel(it) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it.id) },
                        isGrid = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TvHeaderSection(
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = LavenderPrimary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚽",
                    fontSize = 20.sp,
                    color = ActivePurpleStart
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "FIFA WORLD CUP",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = PureWhite
                )
                Text(
                    text = "LIVE TELEVISION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LavenderPrimary,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pulse Lavender stadium indicator
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_alpha"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(ActivePurpleStart.copy(alpha = 0.2f), RoundedCornerShape(50.dp))
                    .border(1.dp, LavenderPrimary.copy(alpha = 0.4f), RoundedCornerShape(50.dp))
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(LavenderPrimary.copy(alpha = alpha), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ONLINE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = LavenderPrimary,
                    letterSpacing = 1.sp
                )
            }

            // Elegant Info Button
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .size(32.dp)
                    .background(LavenderPrimary.copy(alpha = 0.15f), CircleShape)
                    .border(1.2.dp, LavenderPrimary.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "App Information",
                    tint = LavenderPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CreatorInfoDialog(
    onDismiss: () -> Unit
) {
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(LavenderPrimary.copy(alpha = 0.15f), CircleShape)
                        .border(1.5.dp, LavenderPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚽", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "App Information",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = PureWhite
                )
                Text(
                    text = "App Created By Morshed Alam Riday",
                    style = MaterialTheme.typography.bodySmall,
                    color = LavenderPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Developed by Morshed Alam Riday. Converted into an optimized high-fidelity application for world cup channels with smooth adaptive streaming.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightAccentText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Facebook Profile
                CreatorContactCard(
                    title = "Facebook ID",
                    subtitle = "Morshed Alam Riday",
                    icon = "👤",
                    color = Color(0xFF1877F2),
                    onClick = {
                        uriHandler.openUri("https://www.facebook.com/morshedalamridayy")
                    }
                )

                // Facebook Page
                CreatorContactCard(
                    title = "Facebook Page",
                    subtitle = "itzhridayy",
                    icon = "📄",
                    color = Color(0xFF1877F2),
                    onClick = {
                        uriHandler.openUri("https://www.facebook.com/itzhridayy")
                    }
                )

                // Telegram Channel
                CreatorContactCard(
                    title = "Telegram Channel",
                    subtitle = "aimakeappbyhriday",
                    icon = "✈️",
                    color = Color(0xFF0088CC),
                    onClick = {
                        uriHandler.openUri("https://t.me/aimakeappbyhriday")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = LavenderPrimary)
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = SlateCardBg,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun CreatorContactCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SlateCardBg.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, SlateBorder.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color.copy(alpha = 0.15f), CircleShape)
                        .border(1.dp, color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = PureWhite
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = LightAccentText
                    )
                }
            }
            Text(
                text = "OPEN ➔",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun LiveBadgeOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "badge")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badge_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(LiveRed, RoundedCornerShape(50.dp))
                .padding(vertical = 4.dp, horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(PureWhite.copy(alpha = alpha), CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "LIVE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = PureWhite,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun ActiveChannelDetails(
    channel: Channel,
    isMuted: Boolean,
    isFavorite: Boolean,
    onMuteToggle: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SlateBorder.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(ActivePurpleStart, ActivePurpleEnd)
                    )
                )
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = channel.iconEmoji,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = channel.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = PureWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = channel.description.ifEmpty { "Enjoy high-quality FIFA World Cup match and global sports feed." },
                        style = MaterialTheme.typography.bodySmall,
                        color = LightAccentText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Action Row: Mute, Favorite, Share
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute/Unmute with interactive pulsing waves custom graphics instead of missing icons
                    IconButton(
                        onClick = onMuteToggle,
                        modifier = Modifier
                            .testTag("mute_toggle_btn")
                            .size(42.dp)
                            .background(
                                if (isMuted) Color.White.copy(alpha = 0.05f) else LavenderPrimary.copy(alpha = 0.15f),
                                CircleShape
                            )
                            .border(1.dp, if (isMuted) SlateBorder else LavenderPrimary, CircleShape)
                    ) {
                        if (isMuted) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Muted",
                                tint = LightAccentText,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            // Drawing audio sound waves icon manually to ensure compile safety and absolute polish
                            PulseSoundWavesIcon()
                        }
                    }

                    // Add to Favorites button
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .testTag("fav_toggle_btn")
                            .size(42.dp)
                            .background(
                                if (isFavorite) LiveRed.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f),
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (isFavorite) LiveRed else SlateBorder,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite Toggle",
                            tint = if (isFavorite) LiveRed else LightAccentText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Share stream info
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .testTag("share_channel_btn")
                            .size(42.dp)
                            .background(Color.White.copy(alpha = 0.05f), CircleShape)
                            .border(1.dp, SlateBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Channel Link",
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PulseSoundWavesIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "waves")
    val heightScale1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w1"
    )
    val heightScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w2"
    )
    val heightScale3 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w3"
    )

    Row(
        modifier = Modifier
            .size(20.dp)
            .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val soundWaveColor = LavenderPrimary
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight(heightScale1)
                .background(soundWaveColor, RoundedCornerShape(1.dp))
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight(heightScale2)
                .background(soundWaveColor, RoundedCornerShape(1.dp))
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight(heightScale3)
                .background(soundWaveColor, RoundedCornerShape(1.dp))
        )
    }
}

@Composable
fun SearchAndHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                "Search over 60+ TV channels...",
                color = LightAccentText,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = LavenderPrimary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search",
                    tint = PureWhite,
                    modifier = Modifier.clickable { onQueryChange("") }
                )
            }
        },
        modifier = modifier
            .testTag("app_search_field")
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SlateCardBg,
            unfocusedContainerColor = SlateCardBg.copy(alpha = 0.6f),
            focusedTextColor = PureWhite,
            unfocusedTextColor = OffWhiteText,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun CategoryChipsRow(
    selectedCategory: String,
    favoriteCount: Int,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        // Special bookmark chip
        item {
            CategoryChip(
                name = "⭐ Favorites ($favoriteCount)",
                isSelected = selectedCategory == "Favorites",
                onClick = { onCategorySelect("Favorites") }
            )
        }

        // Standard chips
        item {
            CategoryChip(
                name = "All Channels",
                isSelected = selectedCategory == "All",
                onClick = { onCategorySelect("All") }
            )
        }

        items(ChannelsData.categories) { category ->
            CategoryChip(
                name = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelect(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isSelected) ActivePurpleEnd else SlateCardBg
            )
            .border(
                width = 1.dp,
                color = if (isSelected) LavenderPrimary else SlateBorder,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = if (isSelected) LavenderPrimary else LightAccentText,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ChannelsSection(
    channels: List<Channel>,
    selectedChannel: Channel?,
    favoriteIds: Set<String>,
    onChannelClick: (Channel) -> Unit,
    onFavoriteToggle: (Channel) -> Unit,
    isGrid: Boolean,
    modifier: Modifier = Modifier
) {
    if (channels.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "🈚",
                    fontSize = 32.sp
                )
                Text(
                    text = "No channels match your selection.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightAccentText
                )
            }
        }
    } else {
        if (isGrid) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(channels, key = { it.id }) { channel ->
                    ChannelCard(
                        channel = channel,
                        isPlaying = selectedChannel?.id == channel.id,
                        isFavorite = favoriteIds.contains(channel.id),
                        onClick = { onChannelClick(channel) },
                        onFavoriteToggle = { onFavoriteToggle(channel) }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(channels, key = { it.id }) { channel ->
                    ChannelListItem(
                        channel = channel,
                        isPlaying = selectedChannel?.id == channel.id,
                        isFavorite = favoriteIds.contains(channel.id),
                        onClick = { onChannelClick(channel) },
                        onFavoriteToggle = { onFavoriteToggle(channel) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChannelCard(
    channel: Channel,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("channel_item_${channel.id}")
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isPlaying) ActivePurpleStart.copy(alpha = 0.25f) else SlateCardBg
            )
            .border(
                1.3.dp,
                if (isPlaying) LavenderPrimary else SlateBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (isPlaying) ActivePurpleEnd else ActivePurpleStart,
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = channel.iconEmoji,
                        fontSize = 18.sp
                    )
                }

                // Small favorite button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White.copy(alpha = 0.04f), CircleShape)
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) LiveRed else LightAccentText.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Column {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    ),
                    color = PureWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    if (isPlaying) {
                        LiveDot()
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PLAYING",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LavenderPrimary,
                            letterSpacing = 0.5.sp
                        )
                    } else {
                        Text(
                            text = channel.category,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Normal,
                            color = LightAccentText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChannelListItem(
    channel: Channel,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .testTag("channel_item_${channel.id}")
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isPlaying) ActivePurpleStart.copy(alpha = 0.2f) else SlateCardBg
            )
            .border(
                1.dp,
                if (isPlaying) LavenderPrimary else SlateBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(ActivePurpleStart, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = channel.iconEmoji,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = PureWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = channel.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = LightAccentText
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (isPlaying) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(LavenderPrimary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    LiveDot()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LIVE PLAYING",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = LavenderPrimary
                    )
                }
            }

            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) LiveRed else LightAccentText.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun LiveDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseOutExpo),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )
    Box(
        modifier = Modifier
            .size(6.dp)
            .background(LavenderPrimary.copy(alpha = alpha), CircleShape)
    )
}

@Composable
fun LoadingStatePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
            .background(SlateCardBg, RoundedCornerShape(16.dp))
            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = LavenderPrimary,
            modifier = Modifier.size(44.dp)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "World Cup TV: Welcome $name!", modifier = modifier)
}
