package com.capstone.aiskin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.core.helper.VerticalItemDecoration
import com.capstone.aiskin.core.helper.toPx
import com.capstone.aiskin.databinding.FragmentHomeBinding

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

        // Setup RecyclerView Disease
        val diseaseAdapter = DiseaseAdapter(getDummyDiseases())
        binding.rvSkinDisease.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = diseaseAdapter
            setHasFixedSize(true)
        }

    // Set up RecyclerView for Articles
        val articleAdapter = ArticleAdapter(getDummyArticles())
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

    private fun getDummyDiseases(): List<DiseaseItem> {
        return listOf(
            DiseaseItem(
                "https://www.yalemedicine.org/conditions/psoriasis",
                "Psoriasis",
                "Kondisi kulit kronis dengan bercak merah bersisik."
            ),
            DiseaseItem(
                "https://www.honestdocs.id/apa-bedanya-eksim-dan-infeksi-kulit",
                "Eksim",
                "Iritasi kulit menyebabkan kemerahan dan gatal."
            ),
            DiseaseItem(
                "https://www.sidomunculstore.com/blog/post/cara-menghilangkan-jerawat-di-pipi-penyebab-solusi-dan-tips.html",
                "Jerawat",
                "Penyumbatan pori-pori menyebabkan bintik kecil."
            ),
            DiseaseItem(
                "https://www.halodoc.com/kesehatan/kurap",
                "Kurap",
                "Infeksi jamur berbentuk cincin merah di kulit."
            ),
            DiseaseItem(
                "https://www.associatedskincare.com/blog/why-do-i-have-rosacea",
                "Rosacea",
                "Kemerahan kronis di wajah dengan bintil kecil."
            ),
            DiseaseItem(
                "https://www.mitrakeluarga.com/artikel/gejala-herpes-zoster",
                "Herpes Zoster",
                "Ruam menyakitkan disebabkan oleh virus cacar air."
            ),
            DiseaseItem(
                "https://primayahospital.com/kulit-dan-kelamin/melasma/",
                "Melasma",
                "Bercak coklat pada wajah karena paparan matahari."
            ),
            DiseaseItem(
                "https://id.wikipedia.org/wiki/Urtikaria",
                "Urtikaria",
                "Biduran dengan bercak merah dan gatal."
            ),
            DiseaseItem(
                "https://www.alomedika.com/penyakit/dermatovenereologi/skabies",
                "Skabies",
                "Infeksi kutu menyebabkan rasa gatal di malam hari."
            ),
            DiseaseItem(
                "https://www.google.com/imgres?q=vitiligo&imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F7%2F75%2FVitiligo2.JPG&imgrefurl=https%3A%2F%2Fid.wikipedia.org%2Fwiki%2FVitiligo&docid=TQImtyDJ3RGXbM&tbnid=PmcT5IoQjUUr1M&vet=12ahUKEwjd4K6r4PKJAxV1xjgGHVqMMqEQM3oECB0QAA..i&w=4100&h=2910&hcb=2&ved=2ahUKEwjd4K6r4PKJAxV1xjgGHVqMMqEQM3oECB0QAA",
                "Vitiligo",
                "Hilangnya pigmen kulit menyebabkan bercak putih."
            )
        )
    }

    private fun getDummyArticles(): List<ArticleItem> {
        return listOf(
            ArticleItem(
                "https://via.placeholder.com/150",
                "How to Care for Your Skin",
                "Learn the best tips to keep your skin healthy and glowing.",
                "22 Nov 2024"
            ),
            ArticleItem(
                "https://via.placeholder.com/150",
                "Understanding Skin Conditions",
                "An overview of common skin conditions and their treatments.",
                "21 Nov 2024"
            ),
            ArticleItem(
                "https://via.placeholder.com/150",
                "Top Skincare Myths Debunked",
                "Discover the truth about skincare myths you may have believed.",
                "20 Nov 2024"
            )
        )
    }


}
