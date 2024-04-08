package com.weather.clearcast.util

import com.weather.clearcast.model.forecast.WeatherForecastDetail
import java.util.Calendar
import java.util.Date

const val ABSOLUTE_ZER0_KELVIN = 273.15
const val CITY_NAME = "Bengaluru"
const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val WEATHER_TIME = 9
const val FORECAST_DAYS_MAX_COUNT = 4

object RemoteConstants {
    const val APP_ID = "9b8cb8c7f11c077f8c4e217974d9ee40"
    const val WEATHER_ENDPOINT = "/data/2.5/weather"
    const val FORECAST_ENDPOINT = "/data/2.5/forecast"
}

fun Float.convertKelvinToCelsius(): Int = (this - ABSOLUTE_ZER0_KELVIN).toInt()

fun List<WeatherForecastDetail>.filterForecastPerDay(): List<WeatherForecastDetail> {
    return this.filter {
        val calendar: Calendar = Calendar.getInstance()
        val todayDate = calendar.get(Calendar.DATE)
        calendar.time = it.date
        calendar.get(Calendar.HOUR_OF_DAY) == WEATHER_TIME && calendar.get(Calendar.MINUTE) == 0
                &&calendar.get(Calendar.DATE) != todayDate &&
                (calendar.get(Calendar.DATE) - todayDate) <= FORECAST_DAYS_MAX_COUNT
    }
}

fun getDayOfDate(value: Date): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = value
    println("<<< ${calendar.get(Calendar.DAY_OF_WEEK)}")
    fun Int.toDay(): String {
        return when(this) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "N/A"
        }
    }

    return calendar.get(Calendar.DAY_OF_WEEK).toDay()
}

