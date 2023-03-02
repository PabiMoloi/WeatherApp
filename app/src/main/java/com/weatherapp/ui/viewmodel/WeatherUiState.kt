package com.weatherapp.ui.viewmodel

import com.weatherapp.domain.model.FusedWeather

sealed class WeatherUiState {
    object LoadingState : WeatherUiState()
    object Error : WeatherUiState()
    data class Success(val fusedWeather: FusedWeather) : WeatherUiState()
}
