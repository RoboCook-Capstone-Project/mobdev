package com.example.robocook.razif.data.database.helper

import android.content.Context
import com.example.robocook.razif.data.database.recipe.RecipeDatabase
import com.example.robocook.razif.data.database.recipe.RecipeRepository
import com.example.robocook.razif.data.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context): RecipeRepository {

        val database = RecipeDatabase.getDatabase(context)
        val service = ApiConfig.getApiService()
        return RecipeRepository(database, service)

    }

}