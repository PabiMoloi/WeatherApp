package com.weatherapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.api.dto.toCurrentWeather
import com.weatherapp.api.dto.toForecastWeather
import com.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private var _currentWeather = MutableLiveData<CurrentWeatherUiState>()
    val currentWeather: MutableLiveData<CurrentWeatherUiState> get() = _currentWeather

    private var _forecastWeather = MutableLiveData<ForecastWeatherUiState>()
    val forecastWeather: MutableLiveData<ForecastWeatherUiState> get() = _forecastWeather

    fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _currentWeather.value = CurrentWeatherUiState.LoadingState

            val weatherResponse = repository.getCurrentWeather(lat, lon)
            if (weatherResponse.isSuccessful) {
                _currentWeather.value = weatherResponse.body()?.toCurrentWeather()
                    ?.let { CurrentWeatherUiState.Success(it) }
            } else {
                _currentWeather.value = CurrentWeatherUiState.Error
            }
        }
    }

    fun getForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            _forecastWeather.value = ForecastWeatherUiState.LoadingState

            val forecastResponse = repository.getForecast(lat, lon)
            if (forecastResponse.isSuccessful) {
                _forecastWeather.value =
                    forecastResponse.body()?.toForecastWeather()
                        ?.let { ForecastWeatherUiState.Success(it) }
            } else {
                _forecastWeather.value = ForecastWeatherUiState.Error
            }
        }
    }
}