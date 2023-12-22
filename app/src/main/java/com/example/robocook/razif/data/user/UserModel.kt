package com.example.robocook.razif.data.user

data class UserModel(

    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val token: String,
    val isLogin: Boolean

)