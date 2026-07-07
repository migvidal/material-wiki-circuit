package com.migvidal.wikicircuit.core.api.api_service

import com.migvidal.wikicircuit.detail.ArticleResponse
import com.migvidal.wikicircuit.search.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class NetworkApi @Inject constructor(private val httpClient: HttpClient) :
    ApiService {

    override suspend fun getArticle(title: String): ArticleResponse {
        return httpClient.get("") {
            url {
                parameters.apply {
                    append(name = "prop", value = "images|info|pageprops")
                    append(name = "titles", value = title)
                }
            }
        }.body<ArticleResponse>()
    }

    override suspend fun getSearch(term: String): SearchResponse {
        return httpClient.get("") {
            url {
                parameters.apply {
                    append(name = "generator", value = "prefixsearch")
                    append(name = "prop", value = "pageimages|pageterms")
                    append(name = "piprop", value = "thumbnail")
                    append(name = "pithumbsize", value = "50")
                    append(name = "pilimit", value = "10")
                    append(name = "redirects", value = "true")
                    append(name = "wbptterms", value = "description")
                    append(name = "gpslimit", value = "20")
                    append(name = "gpsoffset", value = "0")
                    append(name = "gpssearch", value = term)
                }
            }
        }.body<SearchResponse>()
    }
}