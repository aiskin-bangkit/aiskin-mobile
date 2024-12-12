package com.capstone.aiskin.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import com.capstone.aiskin.core.data.network.article.response.DetailArticleResponse
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    // Latest Article Variables
    private val _latestArticles = MutableLiveData<List<ArticleResponseItem>>()
    val latestArticles: LiveData<List<ArticleResponseItem>> get() = _latestArticles

    private val _isLatestArticleLoading = MutableLiveData<Boolean>()
    val isLatestArticleLoading: LiveData<Boolean> get() = _isLatestArticleLoading

    private val _latestArticlesError = MutableLiveData<String>()
    val latestArticlesError: LiveData<String> get() = _latestArticlesError

    // All Articles Variables
    private val _allArticles = MutableLiveData<List<ArticleResponseItem>>()
    val allArticles: LiveData<List<ArticleResponseItem>> get() = _allArticles

    private val _isAllArticleLoading = MutableLiveData<Boolean>()
    val isAllArticleLoading: LiveData<Boolean> get() = _isAllArticleLoading

    private val _allArticlesError = MutableLiveData<String>()

    // Single Article Variables
    private val _article = MutableLiveData<ArticleResponseItem?>()
    val article: LiveData<ArticleResponseItem?> get() = _article

    private val _isArticleLoading = MutableLiveData<Boolean>()

    private val _articleError = MutableLiveData<String?>()
    val articleError: LiveData<String?> get() = _articleError

    fun fetchLatestArticles() {
        _isLatestArticleLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getLatestArticle()
                _latestArticles.postValue(response)
            } catch (e: Exception) {
                _latestArticlesError.postValue(e.message)
            } finally {
                _isLatestArticleLoading.value = false
            }
        }
    }

    fun fetchAllArticles() {
        _isAllArticleLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getAllArticle()
                _allArticles.postValue(response)
            } catch (e: Exception) {
                _allArticlesError.postValue(e.message)
            } finally {
                _isAllArticleLoading.value = false
            }
        }
    }

    fun fetchArticleById(id: String) {
        _isArticleLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getArticleById(id)
                val mappedArticle = mapDetailToResponse(response, id)
                _article.postValue(mappedArticle)
            } catch (e: Exception) {
                _articleError.postValue(e.message)
            } finally {
                _isArticleLoading.value = false
            }
        }
    }

    private fun mapDetailToResponse(
        detail: DetailArticleResponse,
        id: String
    ): ArticleResponseItem {
        return ArticleResponseItem(
            id = id,
            name = detail.name,
            content = detail.content,
            createdAt = detail.createdAt?.seconds,
            updatedAt = detail.updatedAt?.seconds,
            image = detail.image,
            resource = detail.resource,
            description = detail.description
        )
    }
}
