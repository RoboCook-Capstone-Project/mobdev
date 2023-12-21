package com.example.robocook.razif.ui.add_recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.robocook.razif.data.response.AddRecipeResponse
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddRecipeViewModel(private val userData: UserData) : ViewModel() {

    private val _addRecipeResponse = MutableLiveData<AddRecipeResponse?>()
    val addRecipeResponse: LiveData<AddRecipeResponse?> = _addRecipeResponse

    fun createNewRecipe(
        token: String,
        titleInput: String,
        fileInput: File,
        ingredientsInput: String,
        stepsInput: String) {

        val title = titleInput.toRequestBody("text/plain".toMediaType())
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            fileInput.name,
            fileInput.asRequestBody("image/jpeg".toMediaType())
        )
        val ingredients = ingredientsInput.toRequestBody("text/plain".toMediaType())
        val steps = stepsInput.toRequestBody("text/plain".toMediaType())

        val client = ApiConfig.getApiService().addNewRecipe(token, title, imageMultiPart, ingredients, steps)
        client.enqueue(object : Callback<AddRecipeResponse> {
            override fun onResponse(
                call: Call<AddRecipeResponse>,
                response: Response<AddRecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("AddRecipeViewModel", "Successful response: $responseBody")
                    _addRecipeResponse.value = responseBody
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AddRecipeViewModel", "Error response: $errorBody")
                    _addRecipeResponse.value = null
                }
            }

            override fun onFailure(call: Call<AddRecipeResponse>, t: Throwable) {
                Log.e("AddRecipeViewModel", "Fail to add recipe function")
            }

        })

    }

}