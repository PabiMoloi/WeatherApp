package com.weatherapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.api.dto.toCurrentWeather
import com.weatherapp.api.dto.toForecastWeather
import com.weatherapp.domain.model.ForecastWeather
import com.weatherapp.domain.model.FusedWeather
import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.ui.view.ForecastAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private var _currentWeather = MutableLiveData<WeatherUiState>()
    val currentWeather: MutableLiveData<WeatherUiState> get() = _currentWeather

    private var _forecastWeather = MutableLiveData<ForecastWeatherUiState>()
    val forecastWeather: MutableLiveData<ForecastWeatherUiState> get() = _forecastWeather

    var forecastAdapter = ForecastAdapter(ArrayList())

    fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _currentWeather.value = WeatherUiState.LoadingState

            val weatherResponse = repository.getCurrentWeather(lat, lon)
            val forecastResponse = repository.getForecast(lat, lon)

            if (weatherResponse.isSuccessful && forecastResponse.isSuccessful) {
                val fusedWeather = forecastResponse.body()?.toForecastWeather()?.let {
                    weatherResponse.body()?.toCurrentWeather()?.let { it1 ->
                        FusedWeather(
                            it1,
                            it
                        )
                    }
                }
                _currentWeather.value = fusedWeather?.let { WeatherUiState.Success(it) }
            } else {
                _currentWeather.value = WeatherUiState.Error
            }
        }
    }

    fun setAdapter(forecast: List<ForecastWeather>) {
        forecastAdapter.setItems(forecast)
    }
}