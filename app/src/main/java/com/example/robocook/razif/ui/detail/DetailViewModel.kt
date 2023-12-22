package com.example.robocook.razif.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.robocook.razif.data.user.UserData

class DetailViewModel(private val userData: UserData) : ViewModel() {

    fun fetchToken(): LiveData<String> {
        return userData.fetchUserToken().asLiveData()
    }

}