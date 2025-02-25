package com.example.feedarticlejetpack.network.dtos


import com.squareup.moshi.Json

data class GetArticleResponseDto(
    @Json(name = "status")
    val status: String,
    @Json(name = "article")
    val article: ArticleDto
)