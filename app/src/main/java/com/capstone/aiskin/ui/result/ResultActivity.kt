package com.capstone.aiskin.ui.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.aiskin.R

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
                        setPadding(8, 8, 8, 8)
                    }
                    otherPredictionsContainer.addView(textView)
                }
            } else {
                displayNoOtherPredictions(otherPredictionsContainer)
            }
        } else {
            displayNoOtherPredictions(otherPredictionsContainer)
        }
    }

    private fun displayNoOtherPredictions(container: LinearLayout) {
        val textView = TextView(this).apply {
            text = getString(R.string.text_no_other_predictions)
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.grey))
            setPadding(8, 8, 8, 8)
        }
        container.addView(textView)
    }
}
