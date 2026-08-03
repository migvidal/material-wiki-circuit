package com.migvidal.wikicircuit.core.api.api_service

import androidx.compose.ui.text.intl.Locale
import com.migvidal.wikicircuit.page.common.PageModel
import com.migvidal.wikicircuit.feed.FeedModel
import com.migvidal.wikicircuit.search.SearchModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.util.StringValuesBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class NetworkApi @Inject constructor(private val httpClient: HttpClient) :
    ApiService {

    override suspend fun getPageByTitle(title: String): PageModel {
        return httpClient.get("") {
            url {
                parameters.apply {
                    appendArticleParams()
                    append(name = "titles", value = title)
                }
            }
        }.body()
    }

    override suspend fun getPageById(pageId: Int): PageModel {
        return httpClient.get("") {
            url {
                parameters.apply {
                    appendArticleParams()
                    append(name = "pageids", value = pageId.toString())
                }
            }
        }.body()
    }

    override suspend fun getSearch(term: String): SearchModel {
        return httpClient.get("") {
            url {
                parameters.apply {
                    append(name = "generator", value = "prefixsearch")
                    append(name = "prop", value = "pageimages|pageterms")
                    append(name = "piprop", value = "thumbnail")
                    append(name = "prop", value = "")
                    append(name = "pithumbsize", value = "50")
                    append(name = "pilimit", value = "10")
                    append(name = "redirects", value = "true")
                    append(name = "wbptterms", value = "description")
                    append(name = "gpslimit", value = "20")
                    append(name = "gpsoffset", value = "0")
                    append(name = "gpssearch", value = term)
                }
            }
        }.body<SearchModel>()
    }

    override suspend fun getFeed(forDate: LocalDate): FeedModel {
        return httpClient.get("https://api.wikimedia.org/feed/v1/wikipedia/en/featured/") {
            url {
                val yyyy = forDate.year.toString()
                val mm = forDate.monthValue.paddedLeading()
                val dd = forDate.dayOfMonth.paddedLeading()
                appendPathSegments(yyyy, mm, dd)
            }
        }.body<FeedModel>()
    }
}

private fun StringValuesBuilder.appendArticleParams() {
    append(name = "prop", value = "info|pageprops|images|imageinfo")
    append(name = "inprop", value = "url")
    append(name = "iiprop", value = "url|size")
}

private fun Int.paddedLeading(): String {
    val locale = Locale.current.platformLocale
    return String.format(locale = locale, format = "%02d", this)
}