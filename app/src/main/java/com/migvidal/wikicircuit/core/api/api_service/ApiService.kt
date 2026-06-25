package com.migvidal.wikicircuit.core.api.api_service

import com.migvidal.wikicircuit.detail.FullArticle
import com.migvidal.wikicircuit.search.SearchResponse

interface ApiService {
    suspend fun getArticle(title: String): FullArticle
    suspend fun getSearch(term: String): SearchResponse
}