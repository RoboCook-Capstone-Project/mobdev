package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class PageMeta(

    @field:SerializedName("current_page")
    val current_page: Int? = null,

    @field:SerializedName("total_page")
    val total_page: Int? = null,

    @field:SerializedName("page_size")
    val page_size: Int? = null,

)