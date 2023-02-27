package com.weatherapp.repository

import com.weatherapp.api.ApiService
import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.domain.repository.impl.WeatherRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class WeatherRepositoryTest {

    @Mock
    lateinit var apiService: ApiService
    private lateinit var weatherRepository: WeatherRepository
    private val coroutineDispatcher = Dispatchers.IO

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherRepository = WeatherRepositoryImpl(apiService, coroutineDispatcher)
    }

    @Test
    fun `getCurrentWeather should call api service`() {
        runBlocking {
            weatherRepository.getCurrentWeather(-23.34, 21.09)
            verify(apiService).getWeather(-23.34, 21.09)
        }
    }

    @Test
    fun `getForecast should call api service`() {
        runBlocking {
            weatherRepository.getForecast(-23.34, 21.09)
            verify(apiService).getForecast(-23.34, 21.09)
        }
    }
}