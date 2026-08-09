package com.migvidal.wikicircuit.page.article

import com.migvidal.wikicircuit.core.network.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto

data class Article(
    val title: String,
    val summary: String,
    val mainImg: ImageDto?,
    val images: List<ImageDto>,
)