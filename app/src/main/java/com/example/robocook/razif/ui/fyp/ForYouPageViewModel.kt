package com.example.robocook.razif.ui.fyp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.robocook.razif.data.database.recipe.Recipe
import com.example.robocook.razif.data.database.recipe.RecipeRepository
import com.example.robocook.razif.data.user.UserData
import kotlinx.coroutines.launch

class ForYouPageViewModel(private val userData: UserData, private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _isLoadingMain = MutableLiveData<Boolean>()
    val isLoadingMain: LiveData<Boolean> = _isLoadingMain

    fun fetchToken(): LiveData<String> {

        return userData.fetchUserToken().asLiveData()

    }

    fun userLogout() {

        viewModelScope.launch {
            userData.userLogout()
        }

    }

    fun fetchRecipePaging(token: String): LiveData<PagingData<Recipe>> {

        return recipeRepository.fetchRecipe(token).cachedIn(viewModelScope)

    }

}