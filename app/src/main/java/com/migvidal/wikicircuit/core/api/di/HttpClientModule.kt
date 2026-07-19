package com.migvidal.wikicircuit.core.api.di

import android.content.Context
import android.os.Build
import com.migvidal.wikicircuit.BuildConfig
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.core.USER_AGENT_KEY
import com.migvidal.wikicircuit.core.di.ContextModule
import com.migvidal.wikicircuit.core.getUserAgent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.parseQueryString
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

@Module(includes = [ContextModule::class, JsonModule::class])
@InstallIn(SingletonComponent::class)
class HttpClientModule {
    @Provides
    fun providesHttpClient(context: Context, json: Json): HttpClient {
        return HttpClient(engine = OkHttp.create()) {
            install(ContentNegotiation) {
                json(json = json)
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
                headers {
                    append(USER_AGENT_KEY, getUserAgent(context))
                }
            }
        }
    }


}