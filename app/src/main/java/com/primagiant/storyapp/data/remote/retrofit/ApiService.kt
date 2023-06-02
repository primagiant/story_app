package com.primagiant.storyapp.data.remote.retrofit

import com.primagiant.storyapp.data.remote.response.DetailStoryResponse
import com.primagiant.storyapp.data.remote.response.LoginResponse
import com.primagiant.storyapp.data.remote.response.NewStoryResponse
import com.primagiant.storyapp.data.remote.response.RegisterResponse
import com.primagiant.storyapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories?location=1")
    fun getStoryWithLocation(
        @Header("Authorization") token: String,
        @Query("size") size: Int = 50
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<NewStoryResponse>

}