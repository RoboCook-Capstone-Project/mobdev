package com.example.robocook.razif.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robocook.razif.data.response.LoginResponse
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userData: UserData) : ViewModel() {

    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    fun userLogin(email: String, password: String) {
        _isLoadingLogin.value = true

        val client = ApiConfig.getApiService().userLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val responseBody = response.body()
                _loginResponse.value = responseBody
                _isLoadingLogin.value = false

                if (response.isSuccessful) {
                    if (responseBody?.error == false) {
                        saveUserToken(responseBody.login_result?.token.orEmpty())
                        Log.d("LoginViewModel", "Berhasil login cuy!")
                    } else {
                        Log.e("LoginViewModel", "userLogin failed: ${responseBody?.message}")
                    }
                } else {
                    Log.e("LoginViewModel", "userLogin failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoadingLogin.value = false
                Log.e("LoginViewModel", "userLogin function failed with exception: ${t.message}")
            }

        })
    }

    fun saveUserToken(token: String) {

        viewModelScope.launch(Dispatchers.IO){
            userData.saveUserToken(token)
        }

    }

    fun login() {
        viewModelScope.launch {
        }
    }

}