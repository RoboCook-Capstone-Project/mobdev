package com.example.robocook.razif.data.database.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class Recipe(

    @field:ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @field:ColumnInfo(name = "title")
    val title: String,

    @field:ColumnInfo(name = "author")
    val author: String,

    @field:ColumnInfo(name = "image_url")
    val image_url: String,

    @field:ColumnInfo(name = "ingredients")
    val ingredients: String,

    @field:ColumnInfo(name = "steps")
    val steps: String

)