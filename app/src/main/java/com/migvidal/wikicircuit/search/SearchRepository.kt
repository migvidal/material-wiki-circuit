package com.migvidal.wikicircuit.search

import com.migvidal.wikicircuit.core.api.api_service.ApiService
import com.migvidal.wikicircuit.core.ui.CachedResponse
import com.migvidal.wikicircuit.core.ui.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject

class SearchRepository @Inject constructor(val api: ApiService) {
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
                _response.update {
                    it.copy(
                        status = RequestStatus.Failure(
                            throwable = throwable,
                            message = "Error fetching search results"
                        )
                    )
                }
            }
    }
}

data class CachedSearchResponse(
    override val data: SearchResponse? = null,
    override val lastUpdatedAt: Instant = Instant.now(),
    override val status: RequestStatus = RequestStatus.Success,
) :
    CachedResponse<SearchResponse?>