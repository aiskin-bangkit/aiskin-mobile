package com.capstone.aiskin.core.data.network.authentication

import android.util.Log
import com.capstone.aiskin.core.data.local.datastore.UserPreference
import com.capstone.aiskin.core.data.local.model.UserModel
import com.capstone.aiskin.core.data.network.authentication.response.login.LoginResponse
import com.capstone.aiskin.core.data.network.authentication.response.register.RegisterResponse
import com.capstone.aiskin.core.data.network.retrofit.ApiService
import com.capstone.aiskin.core.data.network.authentication.request.login.LoginRequest
import com.capstone.aiskin.core.data.network.authentication.request.register.RegisterRequest
import kotlinx.coroutines.flow.Flow

class AuthRepository private constructor(
    val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
        Log.d("UserPreference", "tes toket saved: ${user.token}")
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }


    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        val registerRequest = RegisterRequest(name = name, email = email, password = password)
        val response = apiService.register(registerRequest)
        return response
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(email = email, password = password)
        return apiService.login(loginRequest)
    }


    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
