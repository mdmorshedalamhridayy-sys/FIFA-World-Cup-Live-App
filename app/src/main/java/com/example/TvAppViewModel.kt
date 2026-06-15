package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TvAppViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = FavoriteRepository(db.favoriteDao())

    // All channels from static database
    val allChannels = ChannelsData.channelsList

    // UI filters
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")

    // Selected active playing channel
    val selectedChannel = MutableStateFlow<Channel?>(null)

    // Player States
    val isMuted = MutableStateFlow(false)

    init {
        // Initialize with default first channel
        if (allChannels.isNotEmpty()) {
            selectedChannel.value = allChannels.first()
        }
    }

    // Connect favorites from database
    val favoriteIds: StateFlow<Set<String>> = repository.allFavorites
        .combine(MutableStateFlow(Unit)) { favList, _ ->
            favList.map { it.channelId }.toSet()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    // Filtered list of channels based on search and category
    val filteredChannels: StateFlow<List<Channel>> = combine(
        searchQuery,
        selectedCategory,
        favoriteIds
    ) { query, category, favs ->
        allChannels.filter { channel ->
            val matchesCategory = when (category) {
                "All" -> true
                "Favorites" -> favs.contains(channel.id)
                else -> channel.category == category
            }
            val matchesQuery = channel.name.contains(query, ignoreCase = true) || 
                               channel.category.contains(query, ignoreCase = true) ||
                               channel.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = allChannels
    )

    fun selectChannel(channel: Channel) {
        selectedChannel.value = channel
    }

    fun toggleMute() {
        isMuted.value = !isMuted.value
    }

    fun toggleFavorite(channelId: String) {
        viewModelScope.launch {
            val currentFavs = favoriteIds.value
            if (currentFavs.contains(channelId)) {
                repository.removeFavorite(channelId)
            } else {
                repository.addFavorite(channelId)
            }
        }
    }
}
