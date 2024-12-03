package com.capstone.aiskin.ui.detail.disease

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.aiskin.core.helper.DateTimeConverter
import com.capstone.aiskin.databinding.ActivityDetailDiseaseBinding
import com.capstone.aiskin.ui.viewmodel.DiseaseViewModel

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
                val formattedDate = disease.createdAt?.let {
                    DateTimeConverter.formatTimestamp(it)
                } ?: "Unknown Date"

                textDiseaseTitle.text = disease.name
                textDiseaseCreatedAt.text = formattedDate
                textDiseaseDescription.text = disease.description
                textTreatmentRecommendation.text = disease.treatmentRecommendation
                textMedicineRecommendation.text = disease.medicineRecommendation

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