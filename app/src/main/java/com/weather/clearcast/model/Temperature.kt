package com.weather.clearcast.model

import com.google.gson.annotations.SerializedName

data class Temperature(
    @SerializedName("temp")
    val value: Float
)