package com.migvidal.wikicircuit.feature.page.article

import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto

data class Article(
    val title: String,
    val summary: String,
    val images: List<ImageDto>,
)