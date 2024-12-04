package com.capstone.aiskin.core.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.capstone.aiskin.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


@Suppress("DEPRECATION")
class ImageClassifierHelper(
    private var maxResults: Int = 9,
    private val modelName: String = "model_tes.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null


    init {
        setupImageClassifier()
    }


    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(9)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) setupImageClassifier()

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(CastOp(DataType.FLOAT32))
            .build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val results = imageClassifier?.classify(tensorImage)

        if (results != null && results.isNotEmpty()) {
            val classifications = results[0].categories
            if (classifications.isNotEmpty()) {

                val sortedCategories = classifications.sortedByDescending { it.score }

                sortedCategories.forEach { category ->
                    Log.d(TAG, "Label: ${category.label}, Skor: ${category.score}")
                }

                classifierListener?.onResults(results)
            } else {
                classifierListener?.onError("No classifications found.")
            }
        } else {
            classifierListener?.onError("Failed to classify image.")
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

}