package com.capstone.aiskin.ui.detail.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.aiskin.core.data.repository.ArticleRepository


class FavoriteArticleViewModelFactory(private val repository: ArticleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}