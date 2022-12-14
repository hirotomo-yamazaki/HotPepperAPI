package com.example.hotpepperapi.model

data class Results(
    val results_available: Int,
    val shop: List<Shop>
) : java.io.Serializable