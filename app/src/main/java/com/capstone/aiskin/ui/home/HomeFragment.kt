package com.capstone.aiskin.ui.home

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.R
import com.capstone.aiskin.core.di.Injection
import com.capstone.aiskin.databinding.FragmentHomeBinding
import com.capstone.aiskin.ui.account.AccountViewModel
import com.capstone.aiskin.ui.preview.PreviewActivity
import com.capstone.aiskin.core.adapter.ArticleAdapter
import com.capstone.aiskin.core.adapter.DiseaseAdapter
import com.capstone.aiskin.ui.detail.article.DetailArticleActivity
import com.capstone.aiskin.ui.detail.disease.DetailDiseaseActivity
import com.capstone.aiskin.ui.article.ArticleViewModel
import com.capstone.aiskin.ui.authentication.login.LoginActivity
import com.capstone.aiskin.ui.detail.disease.DiseaseViewModel
import com.capstone.aiskin.ui.viewmodel.MainViewModel
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory
import com.capstone.aiskin.core.helper.CameraHelper
import com.capstone.aiskin.core.helper.GalleryHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userToken: String? = null


    private val diseaseViewModel: DiseaseViewModel by viewModels()
    private lateinit var diseaseAdapter: DiseaseAdapter

    private val articleViewModel: ArticleViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    private val accountViewModel: AccountViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel


    private lateinit var pickImage: ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageUri: Uri


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val authRepository = Injection.provideAuthRepository(requireContext())

        val factory = ViewModelFactory(authRepository, )
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        initActivityResultLaunchers()

        mainViewModel.userSession.observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                navigateToLogin()
            } else{
                userToken = user.token
                Log.d("AccountFragment", "${userToken}")
                userToken?.let {
                    if (accountViewModel.userData.value == null) {
                        accountViewModel.fetchUserProfile(it)
                    }
                }
            }
        }

        observeAccountViewModel()


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
                binding.rvSkinDisease.visibility = View.GONE
                binding.rvSkinDiseaseShimmer.visibility = View.VISIBLE
            } else {
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

    private fun observeAccountViewModel() {
        accountViewModel.userData.observe(viewLifecycleOwner) { userData ->
            Log.d("HomeFragment", "User data observed: $userData")
            userData?.let {
                binding.textHeroHome.text = getString(R.string.text_hero_home, it.name ?: "Guest")
            } ?: run {
                binding.textHeroHome.text = getString(R.string.text_hero_home, "Guest")
            }
        }

        accountViewModel.userDataError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
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

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}