package com.migvidal.wikicircuit.core.api.api_service

import com.migvidal.wikicircuit.detail.FullArticle
import com.migvidal.wikicircuit.search.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class NetworkApi @Inject constructor (private val httpClient: HttpClient):
    ApiService {

    override suspend fun getArticle(title: String): FullArticle {
        return FullArticle("TODO", "TODO", "TODO", "TODO")
    }

    override suspend fun getSearch(term: String): SearchResponse {
        // https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&generator=prefixsearch&prop=pageimages|pageterms&piprop=thumbnail&pithumbsize=50&pilimit=10&redirects=true&wbptterms=description&gpslimit=20&gpsoffset=0&gpssearch=NYC
        return httpClient.get("https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&generator=prefixsearch&prop=pageimages|pageterms&piprop=thumbnail&pithumbsize=50&pilimit=10&redirects=true&wbptterms=description&gpslimit=20&gpsoffset=0") {
            parameter(key = "gpssearch", value = term)
        }
            .body<SearchResponse>()
    }
}