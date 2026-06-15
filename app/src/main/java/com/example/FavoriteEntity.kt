package com.example

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val channelId: String,
    val timestamp: Long = System.currentTimeMillis()
)
