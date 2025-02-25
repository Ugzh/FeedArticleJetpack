package com.example.feedarticlejetpack.network.dtos


import com.squareup.moshi.Json

data class OnlyStatusReponseDto(
    @Json(name = "status")
    val status: Int
)