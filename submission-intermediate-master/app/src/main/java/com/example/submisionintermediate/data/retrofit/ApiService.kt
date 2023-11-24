package com.example.submisionintermediate.data.retrofit

import com.example.submisionintermediate.data.response.AddStoryResponse
import com.example.submisionintermediate.data.response.AllStoryResponse
import com.example.submisionintermediate.data.response.DetailStoryResponse
import com.example.submisionintermediate.data.response.LoginResponse
import com.example.submisionintermediate.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email:String,
        @Field("password") password: String
    ):LoginResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): AllStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token:String,
        @Part file:MultipartBody.Part,
        @Part("description") description:RequestBody
    ):AddStoryResponse

    @GET("stories/{id}")
    suspend fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") storyId:String
    ):DetailStoryResponse

}