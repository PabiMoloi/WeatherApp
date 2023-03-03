package com.weatherapp.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.weatherapp.R
import com.weatherapp.common.Constants
import com.weatherapp.databinding.FragmentHomeBinding
import com.weatherapp.domain.model.CurrentWeather
import com.weatherapp.domain.model.ForecastWeather
import com.weatherapp.ui.viewmodel.WeatherUiState
import com.weatherapp.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: WeatherViewModel by viewModels()
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationManager: LocationManager? = null

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocationCoords()
            } else {
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedLocationClient = getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationStatus()
        startObservers()
    }

    private fun startObservers() {
        val weatherObserver: Observer<WeatherUiState> = Observer {
            when (it) {
                WeatherUiState.LoadingState -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                WeatherUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.weatherGroup.visibility = View.GONE
                }
                is WeatherUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.weatherGroup.visibility = View.VISIBLE
                    setCurrentWeather(it.fusedWeather.currentWeather)
                    setForecast(it.fusedWeather.forecast)

                }
            }
        }
        viewModel.currentWeather.observe(viewLifecycleOwner, weatherObserver)
    }

    private fun setupRecyclerView() {
        binding.recyclerviewForecast.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }
    private fun setForecast(forecast: List<ForecastWeather>) {
        setupRecyclerView()
        viewModel.setAdapter(forecast)
        binding.recyclerviewForecast.adapter = viewModel.forecastAdapter
    }

    private fun setCurrentWeather(weather: CurrentWeather) {
        binding.textViewTemperature.text = weather.temperature.toString() +  "\u00B0"
        binding.textViewTempDescription.text = weather.description
        binding.textViewTempCurrent.text = weather.temperature.toString() +  "\u00B0"
        binding.textViewTempMax.text = weather.maximumTemp.toString() +  "\u00B0"
        binding.textViewTempMin.text = weather.minimumTemp.toString() +  "\u00B0"
        when (weather.description) {
            Constants.CLEAR -> {
                binding.imageViewBackground.setImageResource(R.drawable.forest_sunny)
                binding.constraintLayoutBackground.setBackgroundColor(
                    resources.getColor(
                        R.color.sunny,
                        null
                    )
                )
            }
            Constants.CLOUDS -> {
                binding.imageViewBackground.setImageResource(R.drawable.forest_cloudy)
                binding.constraintLayoutBackground.setBackgroundColor(
                    resources.getColor(
                        R.color.cloudy,
                        null
                    )
                )
            }
            Constants.RAIN -> {
                binding.imageViewBackground.setImageResource(R.drawable.forest_rainy)
                binding.constraintLayoutBackground.setBackgroundColor(
                    resources.getColor(
                        R.color.rainy,
                        null
                    )
                )
            }
        }
    }

    private fun getLocationCoords() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        val latitude = location?.latitude
                        val longitude = location?.longitude
                        if (latitude != null && longitude != null) {
                            viewModel.getWeather(latitude, longitude)
                        }
                    }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                showLocationAlertDialog()
            }
            else -> {
                requestLocationPermission()
            }
        }
    }

    private fun checkLocationStatus() {
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocationCoords()
        } else {
            Toast.makeText(requireContext(), "Please enable GPS", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        requestPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun showLocationAlertDialog() {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setPositiveButton(
                    R.string.title_continue
                ) { dialog, id ->
                    dialog.dismiss()
                    requestLocationPermission()
                }
                setNegativeButton(
                    R.string.title_cancel
                ) { dialog, id ->
                    dialog.dismiss()
                }
            }
                .setTitle(R.string.title_location_permission)
                .setMessage(R.string.location_permission_description)
            builder.create()
        }
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}