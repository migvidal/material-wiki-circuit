package com.migvidal.wikicircuit.core.network.api.api_service

import android.content.Context
import android.content.res.Resources
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.feature.page.common.PageModel
import com.migvidal.wikicircuit.feature.feed.FeedModel
import com.migvidal.wikicircuit.feature.search.SearchModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private val FAKE_DELAY = 2.seconds

@OptIn(ExperimentalSerializationApi::class)
class FakeOfflineApi @Inject constructor(@param:ApplicationContext private val context: Context, private val json: Json) :
    ApiService {
    private val resources: Resources = context.resources

    override suspend fun getPageByTitle(title: String): PageModel {
        val inputStream = resources.openRawResource(R.raw.article)
        delay(FAKE_DELAY)
        return json.decodeFromStream<PageModel>(inputStream)
    }

    override suspend fun getPageById(pageId: Int): PageModel {
        val inputStream = resources.openRawResource(R.raw.article)
        delay(FAKE_DELAY)
        return json.decodeFromStream<PageModel>(inputStream)
    }

    override suspend fun getSearch(term: String): SearchModel {
        val inputStream = resources.openRawResource(R.raw.search)
        delay(FAKE_DELAY)
        return json.decodeFromStream<SearchModel>(inputStream)
    }

    override suspend fun getFeed(forDate: LocalDate): FeedModel {
        val inputStream = resources.openRawResource(R.raw.feed)
        delay(FAKE_DELAY)
        return json.decodeFromStream<FeedModel>(inputStream)
    }
}