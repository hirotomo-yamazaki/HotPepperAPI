package com.example.hotpepperapi

import com.example.hotpepperapi.repository.ApiConnection
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {

    const val API_KEY = BuildConfig.API_KEY
    const val FORMAT = "json"
    private const val BASE_URL = "http://webservice.recruit.co.jp/hotpepper/"

    fun retrofit(): ApiConnection {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                ).build()
            )
            .build()

        return retrofit.create(ApiConnection::class.java)
    }
}