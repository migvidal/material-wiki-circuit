package com.migvidal.wikicircuit.search

import android.util.Log
import com.migvidal.wikicircuit.core.network.api.api_service.ApiService
import com.migvidal.wikicircuit.core.ui.CachedResponse
import com.migvidal.wikicircuit.core.ui.RequestStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchProvider @Inject constructor(val api: ApiService) {
    private val _response = MutableStateFlow(CachedSearchResponse())
    val response = _response.asStateFlow()

    suspend fun fetchSearch(term: String) {
        _response.update {
            it.copy(status = RequestStatus.Loading)
        }
        runCatching { api.getSearch(term) }
            .onSuccess { data ->
                _response.update {
                    CachedSearchResponse(data = data)
                }
            }
            .onFailure { throwable ->
                val message = "Error fetching search results"
                Log.e("SearchRepository", message, throwable)
                if (throwable is CancellationException) throw throwable
                _response.update {
                    it.copy(
                        status = RequestStatus.Failure(
                            throwable = throwable,
                            message = message,
                        )
                    )
                }
            }
    }
}

data class CachedSearchResponse(
    override val data: SearchModel? = null,
    override val lastUpdatedAt: Instant = Instant.now(),
    override val status: RequestStatus = RequestStatus.Success,
) :
    CachedResponse<SearchModel?>