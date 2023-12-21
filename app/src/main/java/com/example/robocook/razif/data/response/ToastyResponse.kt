package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class ToastyResponse(

	@field:SerializedName("recipe")
	val recipe: Recipe,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Recipe(

	@field:SerializedName("id")
	val id: String,

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
