package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class AddRecipeResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("recipe")
    val recipe: RecipeList? = null

)