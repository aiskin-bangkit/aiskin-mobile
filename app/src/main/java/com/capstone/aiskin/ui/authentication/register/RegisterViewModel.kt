package com.capstone.aiskin.ui.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.authentication.AuthRepository
import com.capstone.aiskin.core.data.network.authentication.response.register.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    var onRegisterSuccess: ((RegisterResponse) -> Unit)? = null
    var onRegisterError: ((String) -> Unit)? = null

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)

                withContext(Dispatchers.Main) {
                    if (response.error == null || !response.error) {
                        onRegisterSuccess?.invoke(response)
                    } else {
                        onRegisterError?.invoke(response.message ?: "Terjadi kesalahan.")
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    if (errorResponse.message?.contains("Email already exists", true) == true) {
                        "Email sudah terdaftar. Silakan gunakan email lain."
                    } else {
                        errorResponse.message ?: "Terjadi kesalahan."
                    }
                } else {
                    "Terjadi kesalahan saat koneksi ke server."
                }

                withContext(Dispatchers.Main) {
                    onRegisterError?.invoke(errorMessage)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onRegisterError?.invoke("Terjadi kesalahan: ${e.localizedMessage}")
                }
            }
        }
    }


}

