package com.weatherapp.ui.viewmodel

import com.weatherapp.domain.model.CurrentWeather

sealed class CurrentWeatherUiState {
    object LoadingState : CurrentWeatherUiState()
    object Error : CurrentWeatherUiState()
    data class Success(val currentWeather: CurrentWeather) : CurrentWeatherUiState()
}
