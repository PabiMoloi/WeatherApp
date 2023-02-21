package com.weatherapp.api.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("cod")
    var cod: String? = String(),
    @SerializedName("message")
    var message: Int? = null,
    @SerializedName("cnt")
    var cnt: Int? = null,
    @SerializedName("list")
    var list: List<Forecast>? = listOf(),
)
