package com.weather.clearcast.model.forecast

import com.google.gson.annotations.SerializedName
import com.weather.clearcast.model.Temperature
import java.util.Date

data class WeatherForecastDetail(
    @SerializedName("dt_txt")
    val date: Date,
    @SerializedName("main")
    val temperature: Temperature
)