package com.capstone.aiskin.core.data.network.retrofit

import com.capstone.aiskin.core.data.network.authentication.request.login.LoginRequest
import com.capstone.aiskin.core.data.network.authentication.request.register.RegisterRequest
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import com.capstone.aiskin.core.data.network.article.response.DetailArticleResponse
import com.capstone.aiskin.core.data.network.authentication.response.login.LoginResponse
import com.capstone.aiskin.core.data.network.authentication.response.register.RegisterResponse
import com.capstone.aiskin.core.data.network.disease.response.DetailDiseaseResponse
import com.capstone.aiskin.core.data.network.disease.response.DiseaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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




}