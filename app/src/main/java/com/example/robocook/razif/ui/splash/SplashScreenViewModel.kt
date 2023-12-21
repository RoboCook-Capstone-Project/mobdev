package com.example.robocook.razif.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.robocook.razif.data.user.UserData

class SplashScreenViewModel(private val userData: UserData): ViewModel() {

    private val _isLoadingMain = MutableLiveData<Boolean>()
    val isLoadingMain: LiveData<Boolean> = _isLoadingMain
    fun fetchToken(): LiveData<String> {
        return userData.fetchUserToken().asLiveData()
    }
}