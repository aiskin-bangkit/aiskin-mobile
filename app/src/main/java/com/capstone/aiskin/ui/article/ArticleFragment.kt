package com.capstone.aiskin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.core.data.dummy.getAllArticles
import com.capstone.aiskin.core.data.dummy.getDummyNewArticles
import com.capstone.aiskin.databinding.FragmentArticleBinding
import com.capstone.aiskin.ui.home.ArticleAdapter

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val view = binding.root


        val articleAdapter = ArticleAdapter(getAllArticles())
        binding.rvAllArticle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }

        val newArticleAdapter = NewArticleAdapter(getDummyNewArticles())
        binding.rvNewArticle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newArticleAdapter
            setHasFixedSize(true)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}