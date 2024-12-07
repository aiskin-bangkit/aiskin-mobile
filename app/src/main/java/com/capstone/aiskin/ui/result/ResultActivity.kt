package com.capstone.aiskin.ui.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.aiskin.R
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import com.capstone.aiskin.core.data.network.disease.response.DiseaseResponse
import com.capstone.aiskin.utils.normalizeName
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {

    @SuppressLint("StringFormatInvalid", "StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val textResultDisease: TextView = findViewById(R.id.text_result_disease)
        val textResultPercentage: TextView = findViewById(R.id.text_result_percentage)
        val imageResult: ImageView = findViewById(R.id.image_result)
        val textResultContent: TextView = findViewById(R.id.text_result_content)
        val otherPredictionsContainer: LinearLayout = findViewById(R.id.other_predictions_container)
        val textTreatmentRecommendation: TextView = findViewById(R.id.text_treatment_recommendation)
        val textMedicineRecommendation: TextView = findViewById(R.id.text_medicine_recommendation)

        // Data intent
        val prediction = intent.getStringExtra("prediction") ?: getString(R.string.text_result)
        val score = intent.getStringExtra("score")
        val imageUri = intent.getStringExtra("imageUri")
        val otherPredictions = intent.getSerializableExtra("otherPredictions") as? ArrayList<Pair<String, String>>

        textResultDisease.text = prediction
        textResultPercentage.text = getString(R.string.text_result_probability_placeholder, score)

        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imageResult)

        textResultContent.text = getString(R.string.text_loading_description)

        if (!otherPredictions.isNullOrEmpty()) {
            val validPredictions = otherPredictions.filter { (_, percentage) ->
                percentage != "0%"
            }

            if (validPredictions.isNotEmpty()) {
                validPredictions.forEach { (label, percentage) ->
                    val textView = TextView(this).apply {
                        text = getString(R.string.text_prediction_placeholder, label, percentage)
                        textSize = 14f
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                        setPadding(22, 12, 22, 12)
                        background = ContextCompat.getDrawable(context, R.drawable.bg_disease_capsule)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(4, 8, 4, 8)
                        }
                    }
                    otherPredictionsContainer.addView(textView)
                }
            } else {
                displayNoOtherPredictions(otherPredictionsContainer)
            }
        } else {
            displayNoOtherPredictions(otherPredictionsContainer)
        }

        // Ambil data penyakit dari API dan tampilkan
        lifecycleScope.launch {
            val matchedDisease = getMatchedDisease(prediction)
            if (matchedDisease != null) {
                displayDiseaseInfo(matchedDisease, textResultDisease, textResultContent)
                displayTreatmentRecommendation(matchedDisease, textTreatmentRecommendation)
                displayMedicineRecommendation(matchedDisease, textMedicineRecommendation)
            } else {
                displayNoDataFound(textResultContent)
                textTreatmentRecommendation.text = getString(R.string.text_no_recommendation)
                textMedicineRecommendation.text = getString(R.string.text_no_recommendation)
            }
        }
    }

    private fun displayNoOtherPredictions(container: LinearLayout) {
        val textView = TextView(this).apply {
            text = getString(R.string.text_no_other_predictions)
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setPadding(22, 12, 22, 12)
            background = ContextCompat.getDrawable(context, R.drawable.bg_disease_capsule)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(4, 8, 4, 8)
            }
        }
        container.addView(textView)
    }

    private suspend fun getMatchedDisease(predictedLabel: String): DiseaseResponse? {
        return try {
            val apiService = ApiConfig.getApiService()
            val diseases = apiService.getAllDisease()
            diseases.find { it.name?.let { it1 -> normalizeName(it1) } == normalizeName(predictedLabel) }
        } catch (e: Exception) {
            Log.e("ResultActivity", "Error fetching diseases", e)
            null
        }
    }

    private fun displayDiseaseInfo(
        disease: DiseaseResponse,
        diseaseTextView: TextView,
        contentTextView: TextView,
    ) {
        diseaseTextView.text = disease.name
        contentTextView.text = disease.description
    }

    private fun displayTreatmentRecommendation(
        disease: DiseaseResponse,
        treatmentTextView: TextView,
    ) {
        val recommendations = disease.treatmentRecommendation
        treatmentTextView.text = if (!recommendations.isNullOrEmpty()) {
            recommendations.joinToString(separator = "\n")
        } else {
            getString(R.string.text_no_recommendation)
        }
    }

    private fun displayMedicineRecommendation(
        disease: DiseaseResponse,
        medicineTextView: TextView,
    ) {
        val recommendations = disease.medicineRecommendation
        medicineTextView.text = if (!recommendations.isNullOrEmpty()) {
            recommendations.joinToString(separator = "\n")
        } else {
            getString(R.string.text_no_recommendation)
        }
    }

    private fun displayNoDataFound(contentTextView: TextView) {
        contentTextView.text = getString(R.string.text_no_data_found)
    }
}
