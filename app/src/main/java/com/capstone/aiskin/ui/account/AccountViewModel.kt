package com.capstone.aiskin.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.account.response.Data
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel() {

    private val _userData = MutableLiveData<Data?>()
    val userData: MutableLiveData<Data?> get() = _userData

    private val _isUserDataLoading = MutableLiveData<Boolean>()
    val isUserDataLoading: LiveData<Boolean> get() = _isUserDataLoading

    private val _userDataError = MutableLiveData<String>()
    val userDataError: LiveData<String> get() = _userDataError



    fun fetchUserProfile(token : String){
        if (_userData.value != null) {
            return
        }

        _isUserDataLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getUserProfile(token)
                _userData.postValue(response.data)
            } catch (e: Exception){
                _userDataError.postValue(e.message)
                Log.e("AccountViewModel", "Error User Data: ${e.message}", e)
            } finally {
                _isUserDataLoading.value = false
            }
        }
    }

    fun resetUserData() {
        _userData.value = null
    }
}