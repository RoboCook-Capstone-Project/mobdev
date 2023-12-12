package com.example.robocook.razif.data.database.recipe

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.robocook.razif.data.database.remotekeys.RecipeRemoteMediator
import com.example.robocook.razif.data.retrofit.ApiService

class RecipeRepository(private val recipeDatabase: RecipeDatabase, private val apiService: ApiService) {

    fun fetchRecipe(token: String): LiveData<PagingData<Recipe>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(

            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = RecipeRemoteMediator(recipeDatabase, apiService, token),
            pagingSourceFactory = {
                recipeDatabase.recipeDao().fetchAllRecipePaging()
            }

        ).liveData

    }

}