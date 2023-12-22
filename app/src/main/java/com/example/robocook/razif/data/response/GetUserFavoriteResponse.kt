package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName
data class GetUserFavoriteResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("recipe_list")
    val recipe_list: List<RecipeList>

)