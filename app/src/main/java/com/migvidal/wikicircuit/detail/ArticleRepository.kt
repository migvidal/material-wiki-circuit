package com.migvidal.wikicircuit.detail

import com.migvidal.wikicircuit.core.api.api_service.ApiService
import com.migvidal.wikicircuit.core.ui.CachedResponse
import com.migvidal.wikicircuit.core.ui.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject

class ArticleRepository @Inject constructor(val api: ApiService) {
    private val _response = MutableStateFlow(CachedArticleResponse())
    val response = _response.asStateFlow()

    suspend fun fetchArticle(title: String) {
        _response.update {
            it.copy(status = RequestStatus.Loading)
        }
        runCatching {
            api.getArticle(title)
        }.onSuccess { data ->
            _response.update {
                CachedArticleResponse(data = data)
            }
        }.onFailure { throwable ->
            _response.update {
                it.copy(
                    status = RequestStatus.Failure(
                        message = "Could not load article",
                        throwable = throwable,
                    )
                )
            }
        }
    }
}

data class CachedArticleResponse(
    override val data: ArticleResponse? = null,
    override val lastUpdatedAt: Instant = Instant.now(),
    override val status: RequestStatus = RequestStatus.Success
) : CachedResponse<ArticleResponse?>