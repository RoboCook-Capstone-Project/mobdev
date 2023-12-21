package com.example.robocook.razif.ui.fusion

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

class FusionViewModel(private val userData: UserData) : ViewModel() {

    private val _favoriteRecipes = MutableLiveData<List<RecipeList>>()
    val favoriteRecipes: LiveData<List<RecipeList>> get() = _favoriteRecipes

    fun fetchUserFavorites(token: String) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.fetchUserFavorite(token)
                if (!response.error) {
                    _favoriteRecipes.value = response.recipe_list

                } else {
                    Log.e("FusionViewModel", "API error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("FusionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun fetchToken(): LiveData<String> {
        return userData.fetchUserToken().asLiveData()
    }

}