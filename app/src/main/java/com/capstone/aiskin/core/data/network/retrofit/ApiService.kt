package com.capstone.aiskin.core.data.network.retrofit

import com.capstone.aiskin.core.data.network.response.ArticleResponseItem
import com.capstone.aiskin.core.data.network.response.DetailArticleResponse
import com.capstone.aiskin.core.data.network.response.DiseaseResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService{

    @GET("disease")
    suspend fun getAllDisease(): List<DiseaseResponseItem>

    @GET("disease/{id}")
    suspend fun getDiseaseById(
        @Path("id") id: String
    ): DiseaseResponseItem

    @GET("article/latest")
    suspend fun getLatestArticle(): List<ArticleResponseItem>

    @GET("article")
    suspend fun getAllArticle(): List<ArticleResponseItem>

    @GET("article/{id}")
    suspend fun getArticleById(
        @Path("id") id: String
    ): DetailArticleResponse

}