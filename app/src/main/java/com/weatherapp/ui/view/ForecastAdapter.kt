package com.weatherapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.R
import com.weatherapp.common.Constants
import com.weatherapp.common.formatToDayOfWeek
import com.weatherapp.domain.model.ForecastWeather
import javax.inject.Inject

class ForecastAdapter @Inject constructor(
    private var forecast: List<ForecastWeather>
) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var dayOfTheWeek: TextView = view.findViewById(R.id.textViewDay)
        var weatherIcon: ImageView = view.findViewById(R.id.imageViewWeatherIcon)
        var temperature: TextView = view.findViewById(R.id.textViewForecastTemperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val listView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_forecast, parent, false)
        return ForecastViewHolder(listView)
    }

    override fun getItemCount(): Int {
        return forecast.count()
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecastForDay = forecast[position]
        holder.dayOfTheWeek.text = forecastForDay.date?.let { formatToDayOfWeek(it) }
        holder.temperature.text = forecastForDay.temperature.toString() +  "\u00B0"
        when (forecastForDay.description) {
            Constants.RAIN -> holder.weatherIcon.setImageResource(R.drawable.rain)
            Constants.CLEAR -> holder.weatherIcon.setImageResource(R.drawable.clear)
            Constants.CLOUDS -> holder.weatherIcon.setImageResource(R.drawable.partlysunny)
        }
    }
    fun setItems(forecast: List<ForecastWeather>) {
        this.forecast = forecast
        notifyDataSetChanged()
    }

}