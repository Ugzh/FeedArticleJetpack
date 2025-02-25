package com.example.feedarticlejetpack.network.dtos


import com.squareup.moshi.Json

data class RegisterAndLoginResponseDto(
    @Json(name = "status")
    val status: Int,
    @Json(name = "id")
    val idUser: Long,
    @Json(name = "token")
    val token: String?
)