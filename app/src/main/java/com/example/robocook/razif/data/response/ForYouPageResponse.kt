package com.example.robocook.razif.data.response

import com.google.gson.annotations.SerializedName

data class ForYouPageResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("recipe_list")
    val recipe_list: List<RecipeList>? = null,

    @field:SerializedName("page_meta")
    val page_meta: PageMeta? = null

)