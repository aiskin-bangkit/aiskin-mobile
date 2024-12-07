package com.capstone.aiskin.ui.detail.article

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.aiskin.databinding.ActivityDetailArticleBinding
import com.capstone.aiskin.ui.article.ArticleViewModel
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory
import com.capstone.aiskin.utils.DateTimeConverter

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private val articleViewModel: ArticleViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.progressBar.visibility = android.view.View.VISIBLE
        val articleId = intent.getStringExtra("ARTICLE_ID")
        if (articleId != null) {
            articleViewModel.fetchArticleById(articleId)
        } else {
            Log.e("DetailArticleActivity", "Article ID is null")
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        articleViewModel.article.observe(this) { article ->
            with(binding) {
                val formattedCreatedAt = article.createdAt?.let {
                    it.seconds?.let { it1 -> it.nanoseconds?.let { it2 ->
                        DateTimeConverter.formatFirestoreTimestamp(it1,
                            it2
                        )
                    } }
                } ?: "Unknown Date"

                textArticleTitle.text = article.name
                textArticleCreatedAt.text = formattedCreatedAt
                textArticleContent.text = article.content

                Glide.with(this@DetailArticleActivity)
                    .load(article.image ?: "https://via.placeholder.com/150")
                    .into(imageArticle)

                binding.progressBar.visibility = android.view.View.GONE
            }
        }

        articleViewModel.articleError.observe(this) { error ->
            Log.e("DetailArticleActivity", "Error fetching article: $error")
        }
    }
}
