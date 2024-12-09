package com.capstone.aiskin.ui.detail.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.local.article.entity.ArticleEntity
import com.capstone.aiskin.core.data.repository.ArticleRepository
import kotlinx.coroutines.launch

class FavoriteArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    val favoriteArticles = repository.getAllLikedArticles()

    fun saveArticle(article: ArticleEntity) {
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }

    fun removeArticle(article: ArticleEntity) {
        viewModelScope.launch {
            repository.removeArticle(article)
        }
    }

    suspend fun isArticleFavorite(articleId: String): Boolean {
        return repository.isArticleFavorite(articleId)
    }
}


