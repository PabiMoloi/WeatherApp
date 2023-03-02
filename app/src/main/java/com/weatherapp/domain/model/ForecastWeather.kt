package com.weatherapp.domain.model

import java.util.*

data class ForecastWeather(
    var temperature: Int? = null,
    var date: Date? = null,
    var description: String? = null
)
