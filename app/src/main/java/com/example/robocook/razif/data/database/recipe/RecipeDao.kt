package com.example.robocook.razif.data.database.recipe

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe_table")
    fun fetchAllRecipePaging(): PagingSource<Int, Recipe>

    @Query("DELETE FROM recipe_table")
    suspend fun removeAllRecipe()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createNewRecipe(recipe: Recipe)

}