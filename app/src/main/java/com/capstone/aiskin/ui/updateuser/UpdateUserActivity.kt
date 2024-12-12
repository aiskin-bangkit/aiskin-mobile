package com.capstone.aiskin.ui.updateuser

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.aiskin.R
import com.capstone.aiskin.core.helper.FileUtil
import com.capstone.aiskin.core.helper.GalleryHelper
import com.capstone.aiskin.databinding.ActivityUpdateUserBinding
import com.capstone.aiskin.ui.account.AccountViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding
    private val accountViewModel: AccountViewModel by viewModels()
    private var imagePart: MultipartBody.Part? = null
    private var imageUri: Uri? = null
    private lateinit var pickImage: ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivityResultLaunchers()

        val userToken = intent.getStringExtra("USER_TOKEN")
        if (userToken != null) {
            fetchUserInfo(userToken)
        } else {
            Toast.makeText(this, "Token is missing", Toast.LENGTH_SHORT).show()
        }

        binding.btnUpdateImage.setOnClickListener {
            GalleryHelper.pickImage(pickImage)
        }

        binding.btnUpdate.setOnClickListener {
            val updatedName = binding.usernameEditText.text.toString().trim()
            val updatedEmail = binding.emailEditText.text.toString().trim()

            if (userToken != null) {
                updateUserProfile(userToken, updatedName, updatedEmail, imagePart)
            } else {
                Toast.makeText(this, "Unable to update. Missing token.", Toast.LENGTH_SHORT).show()
            }
        }


        accountViewModel.isUpdateSuccessful.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        accountViewModel.isUpdateError.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvCancel.setOnClickListener { finish() }

    }

    private fun initActivityResultLaunchers() {
        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                loadImage(imageUri)
                val file = FileUtil.from(this, it)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imagePart = MultipartBody.Part.createFormData("image_profile", file.name, requestFile)
            } ?: Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImage(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .into(binding.ivImageUser)
    }

    private fun fetchUserInfo(token: String) {
        lifecycleScope.launch {
            accountViewModel.fetchUserProfile(token)
            accountViewModel.userData.observe(this@UpdateUserActivity) { userData ->
                if (userData != null) {
                    val name = userData.name
                    val email = userData.email

                    binding.usernameEditText.setText(name)
                    binding.emailEditText.setText(email)


                    Glide.with(this@UpdateUserActivity)
                        .load(userData.imageProfile ?: R.drawable.intro_image_3)
                        .into(binding.ivImageUser)
                }
            }
        }
    }

    private fun updateUserProfile(token: String, name: String, email: String, imagePart: MultipartBody.Part?) {

        accountViewModel.updateUserProfile(token, name, email, imagePart)

        accountViewModel.isUpdateSuccessful.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
