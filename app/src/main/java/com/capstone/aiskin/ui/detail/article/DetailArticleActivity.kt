package com.capstone.aiskin.ui.detail.article

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.aiskin.R
import com.capstone.aiskin.core.data.local.database.ArticleDatabase
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import com.capstone.aiskin.core.data.repository.ArticleRepository
import com.capstone.aiskin.databinding.ActivityDetailArticleBinding
import com.capstone.aiskin.ui.article.ArticleViewModel
import com.capstone.aiskin.core.helper.ArticleMapper
import com.capstone.aiskin.core.helper.DateTimeConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private val articleViewModel: ArticleViewModel by viewModels()

    private val favoriteArticleViewModel: FavoriteArticleViewModel by lazy {
        val repository = ArticleRepository(ArticleDatabase.getInstance(application).articleDao())
        FavoriteArticleViewModelFactory(repository).create(FavoriteArticleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.toolbar.btnBack.setOnClickListener { finish() }

        val articleId = intent.getStringExtra("ARTICLE_ID") ?: run {
            finish()
            return
        }

        observeFavoriteStatus(articleId)

        binding.progressBar.visibility = View.VISIBLE

        articleViewModel.fetchArticleById(articleId)

        observeViewModel()
    }

    private fun observeViewModel() {
        articleViewModel.article.observe(this) { response ->
            if (response != null) {
                updateUI(response)
            } else {
                binding.progressBar.visibility = View.GONE
                showToast("Gagal memuat artikel")
            }
        }

        articleViewModel.articleError.observe(this) {
            binding.progressBar.visibility = View.GONE
            showToast("Gagal memuat artikel")
        }
    }

    private fun updateUI(response: ArticleResponseItem) {
        response.let {
            val formattedCreatedAt = it.createdAt?.let { createdAt ->
                DateTimeConverter.formatFirestoreTimestamp(createdAt, 0)
            } ?: "Unknown Date"

            binding.textArticleTitle.text = it.name
            binding.textArticleCreatedAt.text = formattedCreatedAt
            binding.textArticleContent.text = it.content

            Glide.with(this)
                .load(it.image ?: "https://via.placeholder.com/150")
                .into(binding.imageArticle)

            binding.progressBar.visibility = View.GONE

            binding.fabLike.setOnClickListener {
                lifecycleScope.launch {
                    val articleEntity = ArticleMapper.responseToEntity(response)
                    val isFavorite = favoriteArticleViewModel.isArticleFavorite(articleEntity.id)
                    if (isFavorite) {
                        favoriteArticleViewModel.removeArticle(articleEntity)
                        withContext(Dispatchers.Main) {
                            showToast("Removed from favorites")
                            updateFabIcon(false)
                        }
                    } else {
                        favoriteArticleViewModel.saveArticle(articleEntity)
                        withContext(Dispatchers.Main) {
                            showToast("Added to favorites")
                            updateFabIcon(true)
                        }
                    }
                }
            }


        }
    }

    private fun observeFavoriteStatus(articleId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val isFavorite = favoriteArticleViewModel.isArticleFavorite(articleId)
            withContext(Dispatchers.Main) {
                updateFabIcon(isFavorite)
            }
        }
    }

    private fun updateFabIcon(isFavorite: Boolean) {
        binding.fabLike.setImageResource(
            if (isFavorite) R.drawable.ic_love_yellow else R.drawable.ic_love_black
        )
        binding.fabLike.imageTintList = null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
