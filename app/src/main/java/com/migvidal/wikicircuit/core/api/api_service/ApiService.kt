package com.migvidal.wikicircuit.core.api.api_service

import com.migvidal.wikicircuit.detail.ArticleModel
import com.migvidal.wikicircuit.feed.FeedModel
import com.migvidal.wikicircuit.search.SearchModel
import java.time.Instant
import java.time.LocalDate

interface ApiService {
    suspend fun getArticle(title: String): ArticleModel
    suspend fun getSearch(term: String): SearchModel
    suspend fun getFeed(forDate: LocalDate): FeedModel
}