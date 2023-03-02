package com.weatherapp.api.dto

import com.google.gson.annotations.SerializedName
import com.weatherapp.common.removeTimeStamp
import com.weatherapp.domain.model.ForecastWeather

data class ForecastResponse(
    @SerializedName("cod")
    var cod: String? = String(),
    @SerializedName("message")
    var message: Int? = null,
    @SerializedName("cnt")
    var cnt: Int? = null,
    @SerializedName("list")
    var list: List<Forecast>? = listOf(),
)

fun ForecastResponse.toForecastWeather(): List<ForecastWeather> {
    val forecastList = ArrayList<ForecastWeather>()
    for (item in list!!) {
        val forecast = ForecastWeather(
            temperature = item.main?.temp?.toInt(),
            date = removeTimeStamp(item.dt_txt),
            description = item.weather[0].main
        )
        forecastList.add(forecast)
    }

    return forecastList.distinctBy { it.date }.toList()
}