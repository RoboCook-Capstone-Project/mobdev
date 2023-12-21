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

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("steps")
	val steps: String
)
