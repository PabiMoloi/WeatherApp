package com.weatherapp.domain.model

data class FusedWeather(
    var currentWeather: CurrentWeather,
    var forecast: List<ForecastWeather> = listOf()
)
