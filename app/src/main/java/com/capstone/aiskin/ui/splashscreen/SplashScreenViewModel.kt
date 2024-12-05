package com.capstone.aiskin.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.aiskin.core.data.network.authentication.AuthRepository
import com.capstone.aiskin.core.data.local.model.UserModel

class SplashScreenViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getUserSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

}
