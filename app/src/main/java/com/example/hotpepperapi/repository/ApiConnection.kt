package com.example.hotpepperapi.repository

import com.example.hotpepperapi.model.StoreDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiConnection {
    @GET("gourmet/v1/")
    fun getStoreList(
        @Query("key") key: String,
        @Query("genre") genreCode: String,
        @Query("keyword") keyword: String,
        @Query("ktai_coupon") coupon: String,
        @Query("free_drink") freeDrink: String,
        @Query("free_food") freeFood: String,
        @Query("format") format: String
    ): Call<StoreDetail>

    @GET("gourmet/v1/")
    fun byLocation(
        @Query("key") key: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("format") format: String
    ): Call<StoreDetail>
}