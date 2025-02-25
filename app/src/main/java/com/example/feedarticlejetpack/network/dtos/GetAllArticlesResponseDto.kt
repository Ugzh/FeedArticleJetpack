package com.example.feedarticlejetpack.network.dtos


import com.squareup.moshi.Json

data class GetAllArticlesResponseDto(
    @Json(name = "status")
    val status: String,
    @Json(name = "articles")
    val articles: List<ArticleDto>
)