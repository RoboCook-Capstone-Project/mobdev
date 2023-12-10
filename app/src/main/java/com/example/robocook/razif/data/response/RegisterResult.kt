package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResult(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

)