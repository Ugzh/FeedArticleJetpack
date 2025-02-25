package com.example.feedarticlejetpack.network

import com.example.feedarticlejetpack.network.dtos.GetAllArticlesResponseDto
import com.example.feedarticlejetpack.network.dtos.GetArticleResponseDto
import com.example.feedarticlejetpack.network.dtos.NewArticleDto
import com.example.feedarticlejetpack.network.dtos.OnlyStatusReponseDto
import com.example.feedarticlejetpack.network.dtos.RegisterAndLoginResponseDto
import com.example.feedarticlejetpack.network.dtos.RegisterDto
import com.example.feedarticlejetpack.network.dtos.UpdateArticleDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @PUT(ApiRoutes.REGISTER)
    suspend fun registerUser(
        @Body loginDto: RegisterDto,
    ) : Response<RegisterAndLoginResponseDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.LOGIN)
    suspend fun logUser(
        @Field("login") login: String,
        @Field("mdp") password: String
    ) : Response<RegisterAndLoginResponseDto>?

    @GET(ApiRoutes.GET_ALL_ARTICLES)
    suspend fun getAllArticles(
        @Query("with_fav") number: Int = 1,
        @Header("token") token: String)
    : Response<GetAllArticlesResponseDto>?

    @POST(ApiRoutes.UPDATE_ARTICLE)
    suspend fun updateArticle(
        @Path("id") idArticle: Long,
        @Header("token") token: String,
        @Body updateArticleDto : UpdateArticleDto
    ) : Response<OnlyStatusReponseDto>?

    @PUT(ApiRoutes.ADD_ARTICLE)
    suspend fun addArticle(
        @Header("token") token: String,
        @Body newArticleDto: NewArticleDto
    ) : Response<OnlyStatusReponseDto>?

    @GET(ApiRoutes.GET_ARTICLE)
    suspend fun getArticle(
        @Header("token") token: String,
        @Query("id") idArticle: Long,
        @Query("with_fav") number: Int = 1
    ) : Response<GetArticleResponseDto>?

    @PUT(ApiRoutes.TOGGLE_FAVORITE)
    suspend fun toggleFavorite(
        @Path("id") idArticle: Long,
        @Header("token") token: String
    ) : Response<OnlyStatusReponseDto>?
}