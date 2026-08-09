package com.migvidal.wikicircuit.page.common

import com.migvidal.wikicircuit.core.network.api.api_service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PageRepository @Inject constructor(val api: ApiService) {
    suspend fun getByTitle(title: String) = api.getPageByTitle(title)
    suspend fun getById(id: Int) = api.getPageById(id)
}