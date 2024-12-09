package com.capstone.aiskin.core.helper

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.capstone.aiskin.core.data.local.article.entity.ArticleEntity
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CameraHelper {

    fun takePicture(
        context: Context,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        takePicture: ActivityResultLauncher<Uri>,
        imageUriCallback: (Uri) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val photoUri = createImageUri(context)
            imageUriCallback(photoUri)
            takePicture.launch(photoUri)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun createImageUri(context: Context): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "temp_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }
}


object GalleryHelper {
    fun pickImage(
        pickImage: ActivityResultLauncher<PickVisualMediaRequest>
    ) {
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

object DateTimeConverter {

    fun formatTimestamp(timestamp: Long, format: String = "dd MMM yyyy, HH:mm:ss"): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatFirestoreTimestamp(seconds: Long, nanoseconds: Int, format: String = "dd MMM yyyy, HH:mm:ss"): String {
        val milliseconds = seconds * 1000 + nanoseconds / 1_000_000
        val date = Date(milliseconds)
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }
}

/**
 * Util buat nyocokin nama dari model ke API
 */
fun normalizeName(name: String): String {
    return name.replace(Regex("\\s*\\(.*?\\)\\s*"), "").trim().lowercase()
}

/**
 * Util buat nyimpen data article ke room db
 */
object ArticleMapper {
    fun responseToEntity(response: ArticleResponseItem): ArticleEntity {
        return ArticleEntity(
            id = response.id ?: "",
            name = response.name ?: "",
            description = response.description ?: "",
            content = response.content ?: "",
            imageUrl = response.image ?: "",
            createdAt = response.createdAt ?: 0L,
            updatedAt = response.updatedAt ?: 0L
        )
    }
}



