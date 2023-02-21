package com.weatherapp.di

import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.domain.repository.impl.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideWeatherRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository
}