package com.migvidal.wikicircuit.feed

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FeedRepository @Inject constructor() {
    private val _response = MutableStateFlow<Result<String>?>(null)
    val response = _response.asStateFlow()

    fun fetchFeed() = _response.update { Result.success("Data fetched") }
}