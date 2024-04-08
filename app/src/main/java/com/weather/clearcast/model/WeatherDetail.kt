package com.weather.clearcast.model

import com.google.gson.annotations.SerializedName

data class WeatherDetail(
    @SerializedName("name")
    val cityName: String,
    @SerializedName("main")
    val temperature: Temperature
)