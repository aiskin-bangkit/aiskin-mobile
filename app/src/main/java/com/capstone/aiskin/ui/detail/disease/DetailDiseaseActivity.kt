package com.capstone.aiskin.ui.detail.disease

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.aiskin.databinding.ActivityDetailDiseaseBinding
import com.capstone.aiskin.utils.DateTimeConverter

class DetailDiseaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDiseaseBinding
    private val diseaseViewModel: DiseaseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.progressBar.visibility = android.view.View.VISIBLE
        val diseaseId = intent.getStringExtra("DISEASE_ID")
        if (diseaseId != null) {
            diseaseViewModel.fetchDiseaseById(diseaseId)
        } else {
            Log.e("DetailDiseaseActivity", "Disease ID is null")
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        diseaseViewModel.disease.observe(this) { disease ->
            with(binding) {
                val formattedCreatedAt = disease.createdAt?.let { DateTimeConverter.formatTimestamp(it) } ?: "Unknown Date"

                binding.textDiseaseCreatedAt.text = formattedCreatedAt

                textDiseaseTitle.text = disease.name
                textDiseaseDescription.text = disease.description

                val treatment = disease.treatmentRecommendation?.joinToString("\n") ?: "No Treatment Info"
                textTreatmentRecommendation.text = treatment

                val medicine = disease.medicineRecommendation?.joinToString("\n") ?: "No Medicine Info"
                textMedicineRecommendation.text = medicine

                Glide.with(this@DetailDiseaseActivity)
                    .load(disease.image ?: "https://via.placeholder.com/150")
                    .into(imageDisease)

                binding.progressBar.visibility = android.view.View.GONE
            }
        }

        diseaseViewModel.diseaseError.observe(this) { error ->
            Log.e("DetailDiseaseActivity", "Error fetching disease: $error")
        }
    }
}
