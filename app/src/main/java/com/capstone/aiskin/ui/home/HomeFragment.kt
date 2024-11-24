package com.capstone.aiskin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.databinding.FragmentHomeBinding
import com.capstone.aiskin.core.data.dummy.getDummyDiseases
import com.capstone.aiskin.core.data.dummy.getDummyNewArticles

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        val diseaseAdapter = DiseaseAdapter(getDummyDiseases())
        binding.rvSkinDisease.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = diseaseAdapter
            setHasFixedSize(true)
        }

        val articleAdapter = ArticleAdapter(getDummyNewArticles())
        binding.rvNewArticle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }


        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
