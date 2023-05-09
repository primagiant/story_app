package com.primagiant.storyapp.data.api

import com.primagiant.storyapp.data.response.AllStoryResponse
import com.primagiant.storyapp.data.response.DetailStoryResponse
import com.primagiant.storyapp.data.response.LoginResponse
import com.primagiant.storyapp.data.response.NewStoryResponse
import com.primagiant.storyapp.data.response.RegisterResponse
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

    @Multipart
    @POST("stories")
    fun addStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<NewStoryResponse>

    @GET("stories")
    fun getAllStory(
    ): Call<AllStoryResponse>

    @GET("stories/{id}")
    fun detailStory(
        @Path("id") id: String
    ): Call<DetailStoryResponse>

}