package com.capstone.aiskin.core.data.local.article.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.capstone.aiskin.core.data.local.article.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: ArticleEntity)

    @Delete
    suspend fun removeArticle(article: ArticleEntity)

    @Query("SELECT * FROM favorite_articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: String): ArticleEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_articles WHERE id = :articleId)")
    suspend fun isArticleFavorite(articleId: String): Boolean

    @Query("SELECT * FROM favorite_articles")
    fun getAllLikedArticles(): LiveData<List<ArticleEntity>>
}
