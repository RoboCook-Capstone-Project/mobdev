package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class RecipeList(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("image_url")
    val image_url: String,

    @field:SerializedName("ingredients")
    val ingredients: String,

    @field:SerializedName("steps")
    val steps: String

)