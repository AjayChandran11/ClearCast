package com.weather.clearcast.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.weather.clearcast.R
import com.weather.clearcast.databinding.CardForecastBinding
import com.weather.clearcast.model.forecast.WeatherForecastDetail
import com.weather.clearcast.util.convertKelvinToCelsius
import com.weather.clearcast.util.getDayOfDate

class ForecastAdapter: RecyclerView.Adapter<ViewHolder>() {
    private val weatherForecastList: MutableList<WeatherForecastDetail> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int = weatherForecastList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = weatherForecastList[position]
        (holder as ForecastViewHolder).bind(forecast)
    }

    fun addForecasts(list: List<WeatherForecastDetail>) {
        weatherForecastList.addAll(list)
        this.notifyItemRangeInserted(0, weatherForecastList.size)
    }
}

class ForecastViewHolder(private val binding: CardForecastBinding,
                         private val context: Context): ViewHolder(binding.root) {
    fun bind(detail: WeatherForecastDetail) {
        binding.day.text = getDayOfDate(detail.date)
        binding.temperature.text = context.getString(
            R.string.temperature_text_celsius,
            detail.temperature.value
                .convertKelvinToCelsius()
        )
    }
}