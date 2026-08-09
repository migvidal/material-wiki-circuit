package com.migvidal.wikicircuit.core.network.api.api_service

import com.migvidal.wikicircuit.feature.page.common.PageModel
import com.migvidal.wikicircuit.feature.feed.FeedModel
import com.migvidal.wikicircuit.feature.search.SearchModel
import java.time.LocalDate

interface ApiService {
    suspend fun getPageByTitle(title: String): PageModel
    suspend fun getPageById(pageId: Int): PageModel
    suspend fun getSearch(term: String): SearchModel
    suspend fun getFeed(forDate: LocalDate): FeedModel
}