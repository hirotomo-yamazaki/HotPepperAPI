package com.example.hotpepperapi.repository

import com.example.hotpepperapi.model.StoreDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiConnection {
    @GET("gourmet/v1/")
    fun getStoreDetail(
        @Query("key") key: String,
        @Query("genre") genreCode: String,
        @Query("keyword") keyword: String,
        @Query("format") format: String
    ): Call<StoreDetail>
}