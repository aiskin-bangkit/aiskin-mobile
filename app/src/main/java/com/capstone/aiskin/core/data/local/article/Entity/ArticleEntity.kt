package com.capstone.aiskin.core.data.local.article.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val createdAt: Long,
    val updatedAt: Long
)