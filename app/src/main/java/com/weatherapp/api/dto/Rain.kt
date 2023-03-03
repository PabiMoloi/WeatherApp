package com.weatherapp.api.dto

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    var oneH : Double? = null
)
