package com.capstone.aiskin.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.capstone.aiskin.core.data.local.article.entity.ArticleEntity
import com.capstone.aiskin.core.data.local.database.ArticleDatabase
import com.capstone.aiskin.core.data.repository.ArticleRepository

class LikedArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArticleRepository

    val likedArticles: LiveData<List<ArticleEntity>>

    init {
        val dao = ArticleDatabase.getInstance(application).articleDao()
        repository = ArticleRepository(dao)
        likedArticles = repository.getAllLikedArticles()
    }
}