package com.migvidal.wikicircuit.core.api.api_service

import android.content.Context
import android.content.res.Resources
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.detail.ArticleModel
import com.migvidal.wikicircuit.feed.FeedModel
import com.migvidal.wikicircuit.search.SearchModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class FakeOfflineApi @Inject constructor(@param:ApplicationContext private val context: Context) :
    ApiService {
    private val resources: Resources = context.resources

    override suspend fun getArticle(title: String): ArticleModel {
        val inputStream = resources.openRawResource(R.raw.article)
        return Json.decodeFromStream<ArticleModel>(inputStream)
    }

    override suspend fun getSearch(term: String): SearchModel {
        val inputStream = resources.openRawResource(R.raw.search)
        return Json.decodeFromStream<SearchModel>(inputStream)
    }

    override suspend fun getFeed(forDate: LocalDate): FeedModel {
        val inputStream = resources.openRawResource(R.raw.feed)
        return Json.decodeFromStream<FeedModel>(inputStream)
    }
}