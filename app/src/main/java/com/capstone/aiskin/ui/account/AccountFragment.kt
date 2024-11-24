package com.capstone.aiskin.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.core.data.dummy.getDummyDiseases
import com.capstone.aiskin.core.data.dummy.getDummyNewArticles
import com.capstone.aiskin.databinding.FragmentAccountBinding
import com.capstone.aiskin.ui.home.ArticleAdapter
import com.capstone.aiskin.ui.home.DiseaseAdapter


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view: View = binding.root

        val diagnosedHistoryAdapter = DiseaseAdapter(getDummyDiseases())
        binding.rvDiagnosedHistory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = diagnosedHistoryAdapter
            setHasFixedSize(true)
        }

        val likedArticleAdapter = ArticleAdapter(getDummyNewArticles())
        binding.rvLikedArticle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = likedArticleAdapter
            setHasFixedSize(true)
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}