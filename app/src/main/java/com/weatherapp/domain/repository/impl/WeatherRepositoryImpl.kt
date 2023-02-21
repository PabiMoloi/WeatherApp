package com.weatherapp.domain.repository.impl

import com.weatherapp.api.ApiService
import com.weatherapp.api.dto.ForecastResponse
import com.weatherapp.api.dto.WeatherResponse
import com.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Response<WeatherResponse> =
        withContext(defaultDispatcher) {
            apiService.getWeather(lat, lon)
        }

    override suspend fun getForecast(lat: Double, lon: Double): Response<ForecastResponse> =
        withContext(defaultDispatcher) {
            apiService.getForecast(lat, lon)
        }
}