package com.capstone.aiskin.core.data.repository

import androidx.lifecycle.LiveData
import com.capstone.aiskin.core.data.local.article.dao.ArticleDao
import com.capstone.aiskin.core.data.local.article.entity.ArticleEntity

class ArticleRepository(private val articleDao: ArticleDao) {

    fun getAllLikedArticles(): LiveData<List<ArticleEntity>> {
        return articleDao.getAllLikedArticles()
    }

    suspend fun saveArticle(article: ArticleEntity) {
        articleDao.saveArticle(article)
    }

    suspend fun removeArticle(article: ArticleEntity) {
        articleDao.removeArticle(article)
    }

    suspend fun isArticleFavorite(articleId: String): Boolean {
        return articleDao.isArticleFavorite(articleId)
    }
}
