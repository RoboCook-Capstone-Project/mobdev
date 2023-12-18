package com.example.robocook.razif.data.database.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.robocook.razif.data.database.helper.Result
import com.example.robocook.razif.data.database.remotekeys.RecipeRemoteMediator
import com.example.robocook.razif.data.response.ToastyResponse
import com.example.robocook.razif.data.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

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

    fun feelingToasty(token: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.feelingToasty(token)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(jsonInString, ToastyResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

}