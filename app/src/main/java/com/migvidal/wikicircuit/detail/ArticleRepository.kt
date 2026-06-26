package com.migvidal.wikicircuit.detail

import com.migvidal.wikicircuit.core.api.api_service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ArticleRepository @Inject constructor(val api: ApiService) {
    private val _article = MutableStateFlow<Result<ArticleResponse?>>(Result.success(null))
    val article = _article.asStateFlow()

    suspend fun fetchArticle(title: String) = _article.update {
        runCatching {
            api.getArticle(title)
        }
    }
}