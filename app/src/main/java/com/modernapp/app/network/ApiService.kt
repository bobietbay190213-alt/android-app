package com.modernapp.app.network

import com.modernapp.app.network.dto.PostDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getPosts(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 20
    ): List<PostDto>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Long): PostDto

    @POST("posts")
    suspend fun createPost(@Body post: PostDto): PostDto

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body post: PostDto
    ): PostDto

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long)
}
