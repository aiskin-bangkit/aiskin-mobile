package com.capstone.aiskin.core.di

import android.content.Context
import com.capstone.aiskin.core.data.local.datastore.UserPreference
import com.capstone.aiskin.core.data.local.datastore.dataStore
import com.capstone.aiskin.core.data.network.authentication.AuthRepository
import com.capstone.aiskin.core.data.network.retrofit.ApiConfig


object Injection {

    fun provideAuthRepository(context: Context): AuthRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiServiceAuth = ApiConfig.getApiService()
        return AuthRepository.getInstance(userPreference, apiServiceAuth)
    }
}
