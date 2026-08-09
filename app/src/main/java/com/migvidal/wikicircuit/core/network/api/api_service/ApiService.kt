package com.migvidal.wikicircuit.core.network.api.api_service

import com.migvidal.wikicircuit.page.common.PageModel
import com.migvidal.wikicircuit.feed.FeedModel
import com.migvidal.wikicircuit.search.SearchModel
import java.time.LocalDate

interface ApiService {
    suspend fun getPageByTitle(title: String): PageModel
    suspend fun getPageById(pageId: Int): PageModel
    suspend fun getSearch(term: String): SearchModel
    suspend fun getFeed(forDate: LocalDate): FeedModel
}