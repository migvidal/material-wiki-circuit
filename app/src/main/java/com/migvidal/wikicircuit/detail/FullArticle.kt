package com.migvidal.wikicircuit.detail

import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class FullArticle(
    val title: String,
    val summary: String,
    val body: String,
    val mainImageUrl: String,
)
