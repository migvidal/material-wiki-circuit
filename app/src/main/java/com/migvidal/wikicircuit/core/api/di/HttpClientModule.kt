package com.migvidal.wikicircuit.core.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.parseQueryString
import io.ktor.http.path
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

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "en.wikipedia.org"
                    pathSegments = listOf("w", "api.php")
                    parameters.append(name = "action", value = "query")
                    parameters.append(name = "format", value = "json")
                    parameters.append(name = "formatversion", value = "2")
                }
            }
        }
    }


}