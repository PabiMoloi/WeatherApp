package com.weatherapp.domain.model

data class CurrentWeather(
    var temperature: Int? = null,
    var maximumTemp: Int? = null,
    var minimumTemp: Int? = null,
    var description: String? = null
)