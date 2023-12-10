package com.example.robocook.razif.data.retrofit

import com.example.robocook.razif.data.response.LoginResponse
import com.example.robocook.razif.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("api/auth/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/auth/register")
    fun userRegister(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

}