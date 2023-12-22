package com.example.robocook.razif.data.database.remotekeys

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.robocook.razif.data.database.recipe.Recipe
import com.example.robocook.razif.data.database.recipe.RecipeDatabase
import com.example.robocook.razif.data.response.RecipeList
import com.example.robocook.razif.data.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class RecipeRemoteMediator(
    private val database: RecipeDatabase,
    private val apiService: ApiService,
    private val token: String
) : RemoteMediator<Int, Recipe>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Recipe>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            var responseData: List<RecipeList>? = null
            responseData = apiService.fetchAllRecipe(token ,page, state.config.pageSize).recipe_list
            val endOfPaginationReached = responseData!!.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().removeRemoteKeys()
                    database.recipeDao().removeAllRecipe()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().putAll(keys)
                val listRecipeItem = responseData!!
                for(recipeAsResponse: RecipeList in listRecipeItem){
                    database.recipeDao().createNewRecipe(
                        Recipe(recipeAsResponse.id, recipeAsResponse.title,
                            recipeAsResponse.author, recipeAsResponse.image_url, recipeAsResponse.ingredients,
                            recipeAsResponse.steps)
                    )
                }

            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: Exception) {

            return MediatorResult.Error(exception)

        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Recipe>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().fetchRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Recipe>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().fetchRemoteKeysId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Recipe>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().fetchRemoteKeysId(data.id)
        }
    }

}