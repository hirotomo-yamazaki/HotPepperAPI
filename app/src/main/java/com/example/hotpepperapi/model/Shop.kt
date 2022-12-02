package com.example.hotpepperapi.model

data class Shop(
    val access: String,
    val address: String,
    val genre: Genre,
    val id: String,
    val lat: Double,
    val lng: Double,
    val logo_image: String,
    val lunch: String,
    val mobile_access: String,
    val name: String,
    val name_kana: String,
    val open: String,
    val photo: Photo,
    val station_name: String,
    val urls: Urls,
):java.io.Serializable