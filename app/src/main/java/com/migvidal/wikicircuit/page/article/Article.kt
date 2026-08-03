package com.migvidal.wikicircuit.page.article

import com.migvidal.wikicircuit.core.api.common_model.ApiImage

data class Article(
    val title: String,
    val summary: String,
    val mainImg: ApiImage?,
    val images: List<ApiImage>,
)