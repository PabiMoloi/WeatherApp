package com.weatherapp.domain.model

data class CurrentWeather(
    var temperature: Double? = null,
    var maximumTemp: Double? = null,
    var minimumTemp: Double? = null,
    var description: String? = null
)