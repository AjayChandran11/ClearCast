package com.weather.clearcast.remote.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.weather.clearcast.util.DATE_FORMAT
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    private const val baseUrl = "https://api.openweathermap.org"

    fun getInstance(): Retrofit {
        val client = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain -> onOnIntercept(chain) }
            .build()
        val gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

@Throws(IOException::class)
private fun onOnIntercept(chain: Chain): Response {
    try {
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    } catch (exception: SocketTimeoutException) {
        exception.printStackTrace()
    }
    return chain.proceed(chain.request())
}