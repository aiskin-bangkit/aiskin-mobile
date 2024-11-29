package com.capstone.aiskin.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aiskin.databinding.FragmentHomeBinding
import com.capstone.aiskin.core.data.dummy.getDummyDiseases
import com.capstone.aiskin.core.data.dummy.getDummyNewArticles
import com.capstone.aiskin.ui.PreviewActivity
import com.capstone.aiskin.utils.CameraHelper
import com.capstone.aiskin.utils.GalleryHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        setupRecyclerView()
        initActivityResultLaunchers()

        binding.btnGallery.setOnClickListener {
            GalleryHelper.pickImage(pickImage, requireContext())
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
}
