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

class FusionResultViewModel(private val userData: UserData) : ViewModel() {

    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>> get() = _recipeList

    fun fetchFusionResult(
        token: String,
        first_recipe_id: Int,
        second_recipe_id: Int) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.fetchFusionResult(token, first_recipe_id, second_recipe_id)
                if (!response.error) {
                    _recipeList.value = response.recipe_list
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