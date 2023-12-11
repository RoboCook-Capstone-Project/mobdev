package com.example.robocook.razif.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.robocook.razif.data.response.RegisterResponse
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val userData: UserData) : ViewModel() {

    private val _isLoadingRegister = MutableLiveData<Boolean>()
    val isLoadingRegister: LiveData<Boolean> = _isLoadingRegister

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    fun userRegister(email: String, name: String, password: String) {

        _isLoadingRegister.value = true

        val client = ApiConfig.getApiService().userRegister(email, name, password)
        client.enqueue(object:Callback<RegisterResponse> {

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                val responseBody = response.body()
                _isLoadingRegister.value = false

                if (response.isSuccessful && responseBody != null) {
                    if (!responseBody.error) {
                        _isRegistered.value = true
                        Log.d("Register ViewModel", "Successfully registered user!")
                    } else {
                        _isRegistered.value = false
                        Log.e("Register ViewModel", "Registration failed: ${responseBody.message}")
                    }
                } else {
                    _isRegistered.value = false
                    Log.e("Register ViewModel", "Registration failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterViewModel", "userRegister function doesn't work")
            }

        })

    }

}