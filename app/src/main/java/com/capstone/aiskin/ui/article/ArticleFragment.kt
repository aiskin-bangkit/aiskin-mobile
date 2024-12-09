package com.capstone.aiskin.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.databinding.FragmentArticleBinding
import com.capstone.aiskin.core.adapter.ArticleAdapter
import com.capstone.aiskin.core.adapter.HeroArticleAdapter
import com.capstone.aiskin.ui.detail.article.DetailArticleActivity

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val articleViewModel: ArticleViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var heroArticleAdapter: HeroArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()
        observeViewModel()

        if (articleViewModel.allArticles.value.isNullOrEmpty()) {
            articleViewModel.fetchAllArticles()
        }

        if (articleViewModel.latestArticles.value.isNullOrEmpty()) {
            articleViewModel.fetchLatestArticles()
        }


        return view
    }

    private fun setupRecyclerView() {
        binding.rvAllArticle.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        binding.rvNewArticle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {

        // All Article Data Observer
        articleViewModel.allArticles.observe(viewLifecycleOwner) { articles ->
            articleAdapter = ArticleAdapter(articles, onItemArticleClick)
            binding.rvAllArticle.adapter = articleAdapter
        }

        articleViewModel.allArticles.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("ArticleFragment", "Error fetching All Article $errorMessage ")
        }

        articleViewModel.isAllArticleLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.rvAllArticle.visibility = View.GONE
                binding.rvAllArticleShimmer.visibility = View.VISIBLE
            } else {
                binding.rvAllArticle.visibility = View.VISIBLE
                binding.rvAllArticleShimmer.visibility = View.GONE
            }
        }

        // Latest Article Data Observer
        articleViewModel.latestArticles.observe(viewLifecycleOwner) { articles ->
            heroArticleAdapter = HeroArticleAdapter(articles, onItemArticleClick)
            binding.rvNewArticle.adapter = heroArticleAdapter
        }

        articleViewModel.latestArticlesError.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("ArticleFragment", "Error fetching Latest Article $errorMessage ")
        }

        articleViewModel.isLatestArticleLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.rvNewArticle.visibility = View.GONE
                binding.rvNewArticleShimmer.visibility = View.VISIBLE
            } else {
                binding.rvNewArticle.visibility = View.VISIBLE
                binding.rvNewArticleShimmer.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onItemArticleClick: (String) -> Unit = { id ->
        val intent = Intent(requireContext(), DetailArticleActivity::class.java).apply {
            putExtra("ARTICLE_ID", id)
        }
        startActivity(intent)
    }
}