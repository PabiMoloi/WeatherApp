package com.weatherapp.domain.repository

import com.weatherapp.api.dto.ForecastResponse
import com.weatherapp.api.dto.WeatherResponse
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double): Response<WeatherResponse>
    suspend fun getForecast(lat: Double, lon: Double): Response<ForecastResponse>
}