package com.weather.clearcast.remote

import com.weather.clearcast.model.WeatherDetail
import com.weather.clearcast.model.forecast.WeatherForecast
import com.weather.clearcast.remote.retrofit.RetrofitBuilder
import com.weather.clearcast.util.CITY_NAME
import com.weather.clearcast.util.RemoteConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApi {
    @GET("${RemoteConstants.WEATHER_ENDPOINT}?q=$CITY_NAME&APPID=${RemoteConstants.APP_ID}")
    suspend fun getCurrentWeatherDetails() : Response<WeatherDetail>

    @GET("${RemoteConstants.FORECAST_ENDPOINT}?q=$CITY_NAME&APPID=${RemoteConstants.APP_ID}")
    suspend fun getWeatherForecastForFourDays(): Response<WeatherForecast>
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object WeatherApiModule {
    @Provides
    fun provideWeatherApiService(): WeatherApi
            = RetrofitBuilder.getInstance().create(WeatherApi::class.java)
}