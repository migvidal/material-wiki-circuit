package com.migvidal.wikicircuit.core.network.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
class JsonModule {
    @Provides
    fun providesJson() = Json { ignoreUnknownKeys = true }
}