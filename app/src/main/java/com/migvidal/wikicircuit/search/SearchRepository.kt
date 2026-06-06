package com.migvidal.wikicircuit.search

import com.migvidal.wikicircuit.core.api.api_service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SearchRepository @Inject constructor(val api: ApiService) {
    private val _response = MutableStateFlow<Result<SearchResponse>?>(null)
    val response = _response.asStateFlow()

    suspend fun fetchSearch() = _response.update { runCatching { api.getSearch() } }
}