package com.example.robocook.razif.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.robocook.razif.data.response.RecipeList
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import kotlinx.coroutines.launch

class FavoriteRecipeViewModel(private val userData: UserData) : ViewModel() {

    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>> get() = _recipeList

    fun fetchUserFavorites(token: String) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.fetchUserFavorite(token)
                if (!response.error) {
                    _recipeList.value = response.recipe_list
                } else {
                    Log.e("FavoriteViewModel", "API returned an error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Exception during API call: ${e.message}", e)
            }
        }
    }

    fun fetchToken(): LiveData<String> {
        return userData.fetchUserToken().asLiveData()
    }

}