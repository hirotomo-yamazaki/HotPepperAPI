package com.example.hotpepperapi.model

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName(value = "results_available")
    val resultsAvailable: Int,
    val shop: List<Shop>
) : java.io.Serializable