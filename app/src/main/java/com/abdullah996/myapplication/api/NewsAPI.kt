package com.abdullah996.myapplication.api

import com.abdullah996.myapplication.models.ApiResponse
import com.abdullah996.myapplication.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getTopNew(
        @Query("country")
        countryCode:String="eg",
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<ApiResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        search:String,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<ApiResponse>
}