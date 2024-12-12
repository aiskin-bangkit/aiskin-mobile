package com.capstone.aiskin.ui.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.aiskin.R
import com.capstone.aiskin.core.data.local.datastore.UserPreference
import com.capstone.aiskin.core.data.local.datastore.dataStore
import com.capstone.aiskin.core.data.network.account.response.AccountData
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import com.capstone.aiskin.core.data.network.disease.response.DiseaseResponse
import com.capstone.aiskin.core.data.network.history.request.HistoryRequest
import com.capstone.aiskin.core.helper.normalizeName
import kotlinx.coroutines.launch
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference

    @SuppressLint("StringFormatInvalid", "StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        enableEdgeToEdge()

        val toolbar = findViewById<View>(R.id.toolbar)
        val btnBack = toolbar.findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }


        val textResultDisease: TextView = findViewById(R.id.text_result_disease)
        val textResultPercentage: TextView = findViewById(R.id.text_result_percentage)
        val imageResult: ImageView = findViewById(R.id.image_result)
        val textResultContent: TextView = findViewById(R.id.text_result_content)
        val otherPredictionsContainer: LinearLayout = findViewById(R.id.other_predictions_container)
        val textTreatmentRecommendation: TextView = findViewById(R.id.text_treatment_recommendation)
        val textMedicineRecommendation: TextView = findViewById(R.id.text_medicine_recommendation)

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

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        val fabSaveToHistory: FloatingActionButton = findViewById(R.id.fab_save_to_history)

        fabSaveToHistory.setOnClickListener {
            lifecycleScope.launch {
                userPreference.getSession().collect { userModel ->
                    val token = userModel.token
                    if (token.isNotEmpty()) {
                        val userAccount = getUserAccount(token)
                        val userId = userAccount?.id ?: "default_user_id"

                        val historyRequest = HistoryRequest(
                            disease_name = textResultDisease.text.toString(),
                            percentage = textResultPercentage.text.toString(),
                            image = imageUri ?: "",
                            created_at = System.currentTimeMillis().toString(),
                            other_suggestion = textTreatmentRecommendation.text.toString(),
                            user_id = userId
                        )

                        saveHistoryToApi(token, historyRequest)
                    } else {
                        Toast.makeText(this@ResultActivity, "Invalid token", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveHistoryToApi(token: String, historyRequest: HistoryRequest) {
        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.saveHistory(token, historyRequest)
                if (response.status == "success") {
                    updateFabIcon(isSaved = true)
                    Toast.makeText(this@ResultActivity, "History saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    updateFabIcon(isSaved = false)
                    Toast.makeText(this@ResultActivity, "Failed to save history", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                val errorMessage = if (e.message?.contains("timeout", ignoreCase = true) == true) {
                    "Request timed out. The server might be busy or down."
                } else {
                    "Failed to save history. Server might be experiencing issues."
                }
                Toast.makeText(this@ResultActivity, errorMessage, Toast.LENGTH_SHORT).show()
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

    /*
     * Fungsi untuk menampilkan keadaan data tidak tersedia
     */
    private fun displayNoDataFound(contentTextView: TextView) {
        contentTextView.text = getString(R.string.text_no_data_found)
    }

    /*
     * Fungsi untuk mengambil data akun pengguna
     * kebutuhan mengambil id-nya
     */
    private suspend fun getUserAccount(token: String): AccountData? {
        return try {
            val apiService = ApiConfig.getApiService()
            val response = apiService.getUserProfile(token)

            if (response.status == "success") {
                response.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun updateFabIcon(isSaved: Boolean) {
        val fabSaveToHistory: FloatingActionButton = findViewById(R.id.fab_save_to_history)
        fabSaveToHistory.setImageResource(
            if (isSaved) R.drawable.ic_love_yellow else R.drawable.ic_love_black
        )
        fabSaveToHistory.imageTintList = null
    }


}
