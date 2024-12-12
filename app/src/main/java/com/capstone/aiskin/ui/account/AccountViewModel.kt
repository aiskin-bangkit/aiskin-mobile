package com.capstone.aiskin.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.account.response.AccountData
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AccountViewModel: ViewModel() {

    private val _userData = MutableLiveData<AccountData?>()
    val userData: MutableLiveData<AccountData?> get() = _userData

    private val _isUserDataLoading = MutableLiveData<Boolean>()
    val isUserDataLoading: LiveData<Boolean> get() = _isUserDataLoading

    private val _userDataError = MutableLiveData<String>()
    val userDataError: LiveData<String> get() = _userDataError

    val isUpdateSuccessful = MutableLiveData<Boolean>()
    val isUpdateError = MutableLiveData<String?>()

    fun fetchUserProfile(token: String) {
        _isUserDataLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getUserProfile(token)

                if (response.status == "success") {
                    _userData.postValue(response.data)
                }
            } catch (e: Exception) {
                _userDataError.postValue(e.message)
            } finally {
                _isUserDataLoading.value = false
            }
        }
    }


    fun updateUserProfile(token: String, name: String, email: String, imageProfile: MultipartBody.Part?) {
        viewModelScope.launch {
            try {

                val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = ApiConfig.getApiService().updateUserProfile(token, nameRequestBody, emailRequestBody, imageProfile)

                if (response.status == "success") {
                    val updatedData = AccountData(
                        id = response.data.id,
                        name = response.data.name,
                        email = response.data.email,
                        imageProfile = response.data.image_profile,
                        token = response.data.token
                    )

                    _userData.postValue(updatedData)
                    isUpdateSuccessful.postValue(true)

                    fetchUserProfile(token)
                } else {
                    isUpdateSuccessful.postValue(false)
                }
            } catch (e: Exception) {
                isUpdateError.postValue(e.message)
                isUpdateSuccessful.postValue(false)
            }
        }
    }
}
