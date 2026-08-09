package com.migvidal.wikicircuit.feature.page.article

import android.util.Log
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.migvidal.wikicircuit.core.ui.CachedResponse
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.feature.page.common.PageModel
import com.migvidal.wikicircuit.feature.page.common.PageModel.PageWithPropsAndImages.ImageReference
import com.migvidal.wikicircuit.feature.page.common.PageRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject

class ArticleProvider @Inject constructor(val pageRepository: PageRepository) {
    private val _response = MutableStateFlow(CachedArticleResponse())
    val response = _response.asStateFlow()

    suspend fun fetchArticle(title: String) {
        fetchArticle { pageRepository.getByTitle(title) }
    }

    suspend fun fetchArticle(id: Int) {
        fetchArticle { pageRepository.getById(id) }
    }

    private suspend fun fetchArticle(method: suspend () -> PageModel) {
        runCatching {
            _response.update {
                it.copy(status = RequestStatus.Loading)
            }
            method()
        }
            .onSuccess { data -> handleSuccess(data) }
            .onFailure { t -> handleFailure(t) }
    }

    private suspend fun handleSuccess(pageModel: PageModel) {
        val page = pageModel.query.pages.firstOrNull()
        if (page == null) {
            _response.update {
                CachedArticleResponse(status = RequestStatus.Failure())
            }
            return
        }
        val imagesData = page.images?.map { getImage(it) } ?: emptyList()
        val images = imagesData.mapNotNull {
            val img = it.query.pages.firstOrNull()?.imageInfo?.firstOrNull()
                ?: return@mapNotNull null
            ImageDto(width = img.width, height = img.height, url = img.source ?: img.url)
        }

        val mainImg = page.imageInfo.firstOrNull()?.run {
            ImageDto(height = height, width = width, url = source ?: url)
        }

        _response.update {
            CachedArticleResponse(
                data = Article(
                    title = page.title,
                    summary = page.pageprops?.wikibaseShortDesc ?: "",
                    mainImg = mainImg,
                    images = images,
                ),
            )
        }
    }

    private suspend fun getImage(imageRef: ImageReference) =
        pageRepository.getByTitle(imageRef.title)

    private fun handleFailure(throwable: Throwable) {
        val message = "Could not load article"
        Log.e("ArticleRepository", message, throwable)
        if (throwable is CancellationException) throw throwable
        _response.update {
            CachedArticleResponse(
                status = RequestStatus.Failure(
                    message = message,
                    throwable = throwable,
                )
            )
        }
    }
}

data class CachedArticleResponse(
    override val data: Article? = null,
    override val lastUpdatedAt: Instant = Instant.now(),
    override val status: RequestStatus = RequestStatus.Success
) : CachedResponse<Article?>