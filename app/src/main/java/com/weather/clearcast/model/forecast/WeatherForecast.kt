package com.weather.clearcast.model.forecast

import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    @SerializedName("list")
    val list: List<WeatherForecastDetail>
)