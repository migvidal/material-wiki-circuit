package com.migvidal.wikicircuit.core.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
class HttpClientModule {

    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true })
            }
        }
    }


}