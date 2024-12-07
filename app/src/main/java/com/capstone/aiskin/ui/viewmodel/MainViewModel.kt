package com.capstone.aiskin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.aiskin.core.data.network.authentication.AuthRepository
import com.capstone.aiskin.core.data.local.model.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val userSession: LiveData<UserModel> = authRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}
