package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class LoginResult(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String? = null

)