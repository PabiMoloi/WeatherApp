package com.weatherapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.api.dto.toCurrentWeather
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
}