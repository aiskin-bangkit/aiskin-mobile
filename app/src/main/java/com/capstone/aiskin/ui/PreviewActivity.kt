package com.capstone.aiskin.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.aiskin.databinding.ActivityPreviewBinding
import com.capstone.aiskin.utils.CameraHelper
import com.capstone.aiskin.utils.GalleryHelper

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private var imageUri: Uri? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var pickImage: ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("imageUri")?.let {
            imageUri = Uri.parse(it)
            loadImage(imageUri)
        }

        initActivityResultLaunchers()

        binding.btnGalleryUlang.setOnClickListener {
            GalleryHelper.pickImage(pickImage, this)
        }

        binding.btnCameraUlang.setOnClickListener {
            CameraHelper.takePicture(this, requestPermissionLauncher, takePicture) { uri ->
                imageUri = uri
            }
        }
    }

    private fun initActivityResultLaunchers() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                CameraHelper.takePicture(this, requestPermissionLauncher, takePicture) { uri ->
                    imageUri = uri
                }
            } else {
                Toast.makeText(this, "Permission denied to access camera", Toast.LENGTH_SHORT).show()
            }
        }

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                loadImage(imageUri)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                loadImage(imageUri)
            } ?: Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImage(uri: Uri?) {
        if (uri != null) {
            Glide.with(this).load(uri).into(binding.previewImageView)
        } else {
            Toast.makeText(this, "Invalid image URI", Toast.LENGTH_SHORT).show()
        }
    }
}
