package com.weather.clearcast.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.clearcast.model.WeatherDetail
import com.weather.clearcast.model.forecast.WeatherForecast
import com.weather.clearcast.remote.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val weatherApi: WeatherApi
): ViewModel() {
    private val _currentWeatherStateFlow: MutableStateFlow<Response<WeatherDetail>?>
                        = MutableStateFlow(null)
    val currentWeatherStateFlow: StateFlow<Response<WeatherDetail>?> =
                        _currentWeatherStateFlow.asStateFlow()
    private val _forecastWeatherStateFlow: MutableStateFlow<Response<WeatherForecast>?>
                        = MutableStateFlow(null)
    val forecastWeatherStateFlow: StateFlow<Response<WeatherForecast>?>
                        = _forecastWeatherStateFlow.asStateFlow()

    init {
        fetchAllWeatherDetails()
    }

    private fun getCurrentWeatherDetails() {
        viewModelScope.launch {
            val weatherInfo = weatherApi.getCurrentWeatherDetails()
            Log.d("WeatherInfo", weatherInfo.body().toString())
            _currentWeatherStateFlow.emit(weatherInfo)
        }
    }

    private fun getWeatherForecastForFourDays() {
        viewModelScope.launch {
            val weatherForecast = weatherApi.getWeatherForecastForFourDays()
            Log.d("WeatherForecast", weatherForecast.body().toString())
            _forecastWeatherStateFlow.emit(weatherForecast)
        }
    }

    fun fetchAllWeatherDetails() {
        getCurrentWeatherDetails()
        getWeatherForecastForFourDays()
    }
}