package com.example.robocook.razif.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.robocook.razif.data.response.RecipeList
import com.example.robocook.razif.data.response.SearchResponse
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val userData: UserData) : ViewModel() {

    private val _isFoundNone = MutableLiveData<Boolean>()
    val isFoundNone: LiveData<Boolean> = _isFoundNone

    private val _searchResult = MutableLiveData<List<RecipeList>?>()
    val searchResult: MutableLiveData<List<RecipeList>?> = _searchResult

    companion object {
        private const val TAG = "SearchViewModel"
    }

    fun fetchToken(): LiveData<String> {
        return userData.fetchUserToken().asLiveData()
    }

    fun searchForRecipe(token: String, keyword: String) {

        _isFoundNone.value = false

        val clientSearch = ApiConfig.getApiService().searchRecipe(token, keyword, 1, 50)
        clientSearch.enqueue(object : Callback<SearchResponse> {

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.isSuccessful) {
                    _searchResult.value = response.body()?.recipe_list
                    if (_searchResult.value?.size == 0) {
                        _isFoundNone.value = true
                    }

                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                Log.e(TAG, "onFailure: ${t.message.toString()}")

            }

        })
    }

}