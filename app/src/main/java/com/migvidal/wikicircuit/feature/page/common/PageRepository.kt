package com.migvidal.wikicircuit.feature.page.common

import com.migvidal.wikicircuit.core.network.api.api_service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PageRepository @Inject constructor(val api: ApiService) {
    suspend fun getByTitle(title: String): PageModel = api.getPageByTitle(title)

    suspend fun getByFilePageUrl(url: String): PageModel {
        val title = url.substringAfter("wiki/")
        return getByTitle(title)
    }
    suspend fun getById(id: Int): PageModel = api.getPageById(id)
}