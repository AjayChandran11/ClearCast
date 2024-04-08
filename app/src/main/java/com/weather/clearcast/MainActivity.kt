package com.weather.clearcast

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.weather.clearcast.databinding.ActivityMainBinding
import com.weather.clearcast.ui.adapter.ForecastAdapter
import com.weather.clearcast.util.convertKelvinToCelsius
import com.weather.clearcast.util.filterForecastPerDay
import com.weather.clearcast.viewmodel.WeatherDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading))
        viewModel = ViewModelProvider(this)[WeatherDetailViewModel::class.java]
        attachDataObservers()
    }

    private fun attachDataObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeatherStateFlow.collect { response ->
                    response?.let {
                        if(response.isSuccessful) {
                            response.body()?.let { weatherInfo ->
                                binding.loading.clearAnimation()
                                binding.loading.visibility = View.GONE
                                binding.city.text = weatherInfo.cityName
                                val currentTemperature = weatherInfo.temperature.value
                                    .convertKelvinToCelsius()
                                binding.temperature.text = applicationContext.getString(
                                    R.string.temperature_text,
                                    currentTemperature
                                )
                            }
                        } else {
                            showErrorSnackBar(applicationContext, binding.root) {
                                binding.loading.visibility = View.VISIBLE
                                viewModel.fetchAllWeatherDetails()
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastWeatherStateFlow.collect { response ->
                    response?.let {
                        if(response.isSuccessful) {
                            response.body()?.let { weatherForecast ->
                                val forecasts = weatherForecast.list.filterForecastPerDay()
                                binding.forecastRecyclerview.apply {
                                    visibility = View.VISIBLE
                                    layoutManager =
                                        LinearLayoutManager(applicationContext)
                                    addItemDecoration(
                                        DividerItemDecoration(
                                            this@MainActivity,
                                            DividerItemDecoration.VERTICAL
                                        )
                                    )
                                }
                                val animation = AnimationUtils.loadAnimation(
                                    this@MainActivity, R.anim.slide_from_bottom)
                                val controller = LayoutAnimationController(animation)
                                controller.order = LayoutAnimationController.ORDER_NORMAL

                                binding.forecastRecyclerview.layoutAnimation = controller
                                val adapter = ForecastAdapter()
                                adapter.addForecasts(forecasts)
                                binding.forecastRecyclerview.adapter = adapter
                            }
                        } else {
                            showErrorSnackBar(applicationContext, binding.root) {
                                binding.loading.visibility = View.VISIBLE
                                viewModel.fetchAllWeatherDetails()
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun showErrorSnackBar(context: Context, view: View,
                              onRetry: () -> Unit) {
    val errorMessage = context.getString(R.string.error_message)
    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
        .setAction(R.string.retry) {
            onRetry.invoke()
        }
        .show()
}