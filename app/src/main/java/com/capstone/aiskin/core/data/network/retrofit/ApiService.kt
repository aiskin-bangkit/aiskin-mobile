package com.capstone.aiskin.core.data.network.retrofit

import com.capstone.aiskin.core.data.network.account.response.AccountResponse
import com.capstone.aiskin.core.data.network.account.response.UpdateResponse
import com.capstone.aiskin.core.data.network.authentication.request.login.LoginRequest
import com.capstone.aiskin.core.data.network.authentication.request.register.RegisterRequest
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import com.capstone.aiskin.core.data.network.article.response.DetailArticleResponse
import com.capstone.aiskin.core.data.network.authentication.response.login.LoginResponse
import com.capstone.aiskin.core.data.network.authentication.response.register.RegisterResponse
import com.capstone.aiskin.core.data.network.disease.response.DetailDiseaseResponse
import com.capstone.aiskin.core.data.network.disease.response.DiseaseResponse
import com.capstone.aiskin.core.data.network.history.request.HistoryRequest
import com.capstone.aiskin.core.data.network.history.response.HistoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService{

    @GET("disease")
    suspend fun getAllDisease(): List<DiseaseResponse>

    @GET("disease/{id}")
    suspend fun getDiseaseById(
        @Path("id") id: String
    ): DetailDiseaseResponse

    @GET("article/latest")
    suspend fun getLatestArticle(): List<ArticleResponseItem>

    @GET("article")
    suspend fun getAllArticle(): List<ArticleResponseItem>

    @GET("article/{id}")
    suspend fun getArticleById(
        @Path("id") id: String
    ): DetailArticleResponse

    @POST("auth/signup")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("auth/signin")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): AccountResponse

    @Multipart
    @PATCH("user/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part imageProfile: MultipartBody.Part?
    ): UpdateResponse


    @GET("history")
    suspend fun getHistory(
    ): AccountResponse

    @POST("history")
    suspend fun saveHistory(
        @Header("Authorization") token: String,
        @Body historyRequest: HistoryRequest
    ): HistoryResponse


}