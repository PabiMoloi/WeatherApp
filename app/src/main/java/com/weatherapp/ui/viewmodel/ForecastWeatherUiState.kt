package com.weatherapp.ui.viewmodel

import com.weatherapp.domain.model.ForecastWeather

sealed class ForecastWeatherUiState {
    object LoadingState : ForecastWeatherUiState()
    object Error : ForecastWeatherUiState()
    data class Success(val forecastWeather: List<ForecastWeather>) : ForecastWeatherUiState()
}
