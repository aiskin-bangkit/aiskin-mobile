package com.capstone.aiskin.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.local.model.UserModel
import com.capstone.aiskin.core.data.network.authentication.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException


class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<UserModel?>()
    val loginResult: LiveData<UserModel?> = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (!response.error) {
                    val token = response.token
                    if (token != null) {
                        val user = UserModel(email, token )
                        saveSession(user)
                        _loginResult.postValue(user)
                    } else {
                        _errorMessage.postValue("Token tidak ditemukan dalam respons.")
                    }
                } else {
                    _errorMessage.postValue(response.message)
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _errorMessage.postValue("401 Unauthorized")
                } else {
                    _errorMessage.postValue("Gagal login: ${e.message}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }

}
