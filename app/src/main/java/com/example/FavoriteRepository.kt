package com.example

import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    val allFavorites: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    suspend fun addFavorite(channelId: String) {
        favoriteDao.addFavorite(FavoriteEntity(channelId))
    }

    suspend fun removeFavorite(channelId: String) {
        favoriteDao.removeFavorite(channelId)
    }
}
