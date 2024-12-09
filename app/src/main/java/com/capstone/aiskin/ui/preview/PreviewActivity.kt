package com.capstone.aiskin.ui.preview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.aiskin.MainActivity
import com.capstone.aiskin.core.helper.ImageClassifierHelper
import com.capstone.aiskin.databinding.ActivityPreviewBinding
import com.capstone.aiskin.ui.result.ResultActivity
import com.capstone.aiskin.core.helper.CameraHelper
import com.capstone.aiskin.core.helper.GalleryHelper

import org.tensorflow.lite.task.vision.classifier.Classifications

class PreviewActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    private lateinit var binding: ActivityPreviewBinding
    private var imageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var pickImage: ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )

        intent.getStringExtra("imageUri")?.let {
            imageUri = Uri.parse(it)
            loadImage(imageUri)
        }

        initActivityResultLaunchers()

        binding.btnGalleryUlang.setOnClickListener {
            GalleryHelper.pickImage(pickImage)
        }

        binding.btnCameraUlang.setOnClickListener {
            CameraHelper.takePicture(this, requestPermissionLauncher, takePicture) { uri ->
                imageUri = uri
            }
        }

        binding.btnAnalyze.setOnClickListener {
            analyzeImage()
        }
    }

    private fun analyzeImage() {
        imageUri?.let { uri ->
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: run {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onResults(results: List<Classifications>?) {
        if (!results.isNullOrEmpty()) {
            val classifications = results[0].categories
            if (classifications.isNotEmpty()) {
                val sortedCategories = classifications.sortedByDescending { it.score }
                val topCategory = sortedCategories[0]
                val percentageScore = topCategory.score * 100
                val score = String.format("%.0f%%", percentageScore)
                val prediction = topCategory.label
                val imageUriString = imageUri.toString()

                if (percentageScore < 90) {
                    showRetryDialog()
                } else {
                    val otherPredictions = sortedCategories
                        .drop(1)
                        .take(3)
                        .filter { it.score > 0 }
                        .map { it.label to String.format("%.0f%%", it.score * 100) }

                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("imageUri", imageUriString)
                        putExtra("prediction", prediction)
                        putExtra("score", score)
                        putExtra("otherPredictions", ArrayList(otherPredictions))
                    }
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Tidak ada hasil klasifikasi.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Hasil klasifikasi kosong.", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    * Fungsi untuk menampilkan dialog
    */
    private fun showRetryDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gambar Tidak Valid")
            .setMessage("Gambar tidak dapat dikenali sebagai penyakit yang tersedia atau penyakit belum terdaftar. Silakan coba masukkan gambar lain.")
            .setPositiveButton("Coba Lagi") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                goToMainActivity()
                dialog.dismiss()
            }
            .show()
    }
    override fun onError(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
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

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
