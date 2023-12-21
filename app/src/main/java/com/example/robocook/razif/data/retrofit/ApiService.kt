package com.example.robocook.razif.data.retrofit

import com.example.robocook.razif.data.response.AddRecipeResponse
import com.example.robocook.razif.data.response.ForYouPageResponse
import com.example.robocook.razif.data.response.LoginResponse
import com.example.robocook.razif.data.response.AddToFavoriteResponse
import com.example.robocook.razif.data.response.GetUserFavoriteResponse
import com.example.robocook.razif.data.response.SearchResponse
import com.example.robocook.razif.data.response.ToastyResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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
    ): Call<AddToFavoriteResponse>

    @GET("api/recipes")
    suspend fun fetchAllRecipe(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ForYouPageResponse

    @FormUrlEncoded
    @POST("api/recipes/favorites")
    fun addRecipeToFavorite(
        @Header("Authorization") token: String,
        @Field("recipe_id") recipe_id: Int,
    ): Call<AddToFavoriteResponse>

    @GET("api/recipes/favorites")
    suspend fun fetchUserFavorite(
        @Header("Authorization") token: String,
    ): GetUserFavoriteResponse

    @GET("api/recipes/toasty")
    suspend fun feelingToasty(
        @Header("Authorization") token: String
    ): ToastyResponse

    @Multipart
    @POST("api/recipes")
    fun addNewRecipe(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part file : MultipartBody.Part,
        @Part("ingredients") ingredients: RequestBody,
        @Part("steps") steps: RequestBody,
    ): Call<AddRecipeResponse>

    @GET("api/recipes/search")
    fun searchRecipe(
        @Header("Authorization") token: String,
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<SearchResponse>

}