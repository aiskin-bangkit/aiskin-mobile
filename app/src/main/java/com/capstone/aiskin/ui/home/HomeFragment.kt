package com.capstone.aiskin.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.databinding.FragmentHomeBinding
import com.capstone.aiskin.ui.preview.PreviewActivity
import com.capstone.aiskin.ui.adapter.ArticleAdapter
import com.capstone.aiskin.ui.adapter.DiseaseAdapter
import com.capstone.aiskin.ui.authentication.login.LoginViewModel
import com.capstone.aiskin.ui.detail.article.DetailArticleActivity
import com.capstone.aiskin.ui.detail.disease.DetailDiseaseActivity
import com.capstone.aiskin.ui.article.ArticleViewModel
import com.capstone.aiskin.ui.detail.disease.DiseaseViewModel
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory
import com.capstone.aiskin.utils.CameraHelper
import com.capstone.aiskin.utils.GalleryHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val diseaseViewModel: DiseaseViewModel by viewModels()
    private lateinit var diseaseAdapter: DiseaseAdapter

    private val articleViewModel: ArticleViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var pickImage: ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageUri: Uri

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()
        initActivityResultLaunchers()

        val textHeroHome = binding.textHeroHome

        loginViewModel.loginResult.observe(viewLifecycleOwner) { user ->
            user?.let {
                // TODO ganti pake name dari user
//                textHeroHome.text = "Hello ${it.name}, Don’t Forget to check your skin health"
                textHeroHome.text = "Hello ${it.email}, Don’t Forget to check your skin health"
            }
        }

        if (diseaseViewModel.diseaseList.value.isNullOrEmpty()) {
            diseaseViewModel.fetchDiseases()
        }

        if (articleViewModel.latestArticles.value.isNullOrEmpty()) {
            articleViewModel.fetchLatestArticles()
        }

        binding.btnGallery.setOnClickListener {
            GalleryHelper.pickImage(pickImage)
        }

        binding.btnCamera.setOnClickListener {
            CameraHelper.takePicture(
                requireContext(),
                requestPermissionLauncher,
                takePicture
            ) { uri ->
                imageUri = uri
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvSkinDisease.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        binding.rvNewArticle.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        /**
         * Fungsi untuk Observer Data Disease
         */
        diseaseViewModel.diseaseList.observe(viewLifecycleOwner) { diseases ->
            if (diseases != null) {
                Log.d("HomeFragment", "Diseases observed: ${diseases.size}")
                diseaseAdapter = DiseaseAdapter(diseases, onItemDiseaseClick)
                binding.rvSkinDisease.adapter = diseaseAdapter
            } else {
                Log.e("HomeFragment", "No diseases found.")
            }
        }



    diseaseViewModel.diseaseListError.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
        }

        diseaseViewModel.isDiseaseListLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                Log.d("HomeFragment", "Disease list loading...")
                binding.rvSkinDisease.visibility = View.GONE
                binding.rvSkinDiseaseShimmer.visibility = View.VISIBLE
            } else {
                Log.d("HomeFragment", "Disease list loaded.")
                binding.rvSkinDisease.visibility = View.VISIBLE
                binding.rvSkinDiseaseShimmer.visibility = View.GONE
            }
        }

        /**
         * Fungsi untuk Observer Data Article
         */
        articleViewModel.latestArticles.observe(viewLifecycleOwner) { article ->
            articleAdapter = ArticleAdapter(article, onItemArticleClick)
            binding.rvNewArticle.adapter = articleAdapter
        }

        articleViewModel.latestArticlesError.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
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

    private fun initActivityResultLaunchers() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                CameraHelper.takePicture(requireContext(), requestPermissionLauncher, takePicture) { uri ->
                    imageUri = uri
                }
            } else {
                Toast.makeText(requireContext(), "Permission denied to access camera", Toast.LENGTH_SHORT).show()
            }
        }

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val intent = Intent(requireContext(), PreviewActivity::class.java).apply {
                    putExtra("imageUri", imageUri.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                val intent = Intent(requireContext(), PreviewActivity::class.java).apply {
                    putExtra("imageUri", uri.toString())
                }
                startActivity(intent)
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

    private val onItemDiseaseClick: (String) -> Unit = { id ->
        Log.d("HomeFragment", "Selected Disease ID: $id")
        val intent = Intent(requireContext(), DetailDiseaseActivity::class.java).apply {
            putExtra("DISEASE_ID", id)
        }
        startActivity(intent)
    }

}
