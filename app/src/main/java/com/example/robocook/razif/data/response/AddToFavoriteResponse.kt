package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class AddToFavoriteResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)