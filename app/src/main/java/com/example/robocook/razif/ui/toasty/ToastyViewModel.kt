package com.example.robocook.razif.ui.toasty

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.example.robocook.razif.data.response.Recipe
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import kotlinx.coroutines.launch

class ToastyViewModel(private val userData: UserData): ViewModel() {

    private val _toasty = MutableLiveData<Recipe>()
    val toasty: LiveData<Recipe> get() = _toasty

    fun feelingToasty(token: String) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.feelingToasty(token)
                if (!response.error) {
                    _toasty.value = response.recipe
                } else {
                    Log.e("ToastyViewModel", "API error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun fetchToken(): LiveData<String> {
        return userData.fetchUserToken().asLiveData()
    }
}