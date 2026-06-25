package com.migvidal.wikicircuit.core.api.api_service

import android.content.Context
import android.content.res.Resources
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.core.api.exception.NullResponseException
import com.migvidal.wikicircuit.detail.FullArticle
import com.migvidal.wikicircuit.search.SearchResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class FakeOfflineApi @Inject constructor (@param:ApplicationContext private val context: Context):
    ApiService {
    private val resources: Resources = context.resources

    override suspend fun getArticle(title: String): FullArticle {
        val inputStream = resources.openRawResource(R.raw.articles)
        val articles = Json.decodeFromStream<List<FullArticle>>(inputStream)
        return articles.find { it.title == title } ?: throw NullResponseException("article")
    }

    override suspend fun getSearch(term: String): SearchResponse {
        val inputStream = resources.openRawResource(R.raw.search)
        return Json.decodeFromStream<SearchResponse>(inputStream)
    }
}