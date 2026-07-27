package com.migvidal.wikicircuit.article

import android.util.Log
import com.migvidal.wikicircuit.core.api.api_service.ApiService
import com.migvidal.wikicircuit.core.ui.CachedResponse
import com.migvidal.wikicircuit.core.ui.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(val api: ApiService) {
    private val _response = MutableStateFlow(CachedArticleResponse())
    val response = _response.asStateFlow()

    suspend fun fetchArticle(title: String) {
        fetchArticle { api.getArticleByTitle(title) }
    }

    suspend fun fetchArticle(pageId: Int) {
        fetchArticle { api.getArticleById(pageId) }
    }

    private suspend fun fetchArticle(method: suspend () -> ArticleModel) {
        _response.update {
            it.copy(status = RequestStatus.Loading)
        }
        runCatching {
            method()
        }.onSuccess { data ->
            _response.update {
                CachedArticleResponse(data = data)
            }
        }.onFailure { throwable ->
            val message = "Could not load article"
            Log.e("ArticleRepository", message, throwable)
            _response.update {
                it.copy(
                    status = RequestStatus.Failure(
                        message = message,
                        throwable = throwable,
                    )
                )
            }
        }
    }
}

data class CachedArticleResponse(
    override val data: ArticleModel? = null,
    override val lastUpdatedAt: Instant = Instant.now(),
    override val status: RequestStatus = RequestStatus.Success
) : CachedResponse<ArticleModel?>