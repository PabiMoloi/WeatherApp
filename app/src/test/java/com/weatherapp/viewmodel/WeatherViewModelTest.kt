package com.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.weatherapp.api.dto.*
import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.ui.viewmodel.WeatherViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: WeatherRepository

    private val viewModel by lazy { WeatherViewModel(repository) }

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should call repository to get current weather`() = runTest {
        mockCurrentWeather()
        viewModel.getWeather(-24.87, -21.34)
        Mockito.verify(repository).getCurrentWeather(-24.87, -21.34)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should call repository to get forecast`() = runTest {
        mockForecast()
        viewModel.getForecast(-24.87, -21.34)
        Mockito.verify(repository).getForecast(-24.87, -21.34)
    }

    private suspend fun mockCurrentWeather() {
        val currentWeather = WeatherResponse(
            Coord(-24.87, -21.34),
            arrayListOf(Weather(802, "cloud", "cloudy", "03d")),
            "stations",
            Main()
        )
        Mockito.doReturn(Response.success(currentWeather)).`when`(repository)
            .getCurrentWeather(-24.87, -21.34)
    }

    private suspend fun mockForecast() {
        val forecast = ForecastResponse(
            list = listOf(
                Forecast(
                    main = Main(),
                    dt_txt = "2023-02-26 15:00:00",
                    weather = arrayListOf(Weather(802, "cloud", "cloudy", "03d"))
                )
            )
        )
        Mockito.doReturn(Response.success(forecast)).`when`(repository)
            .getForecast(-24.87, -21.34)
    }
}