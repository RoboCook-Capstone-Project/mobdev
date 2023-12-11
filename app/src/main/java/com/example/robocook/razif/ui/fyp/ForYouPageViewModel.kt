package com.example.robocook.razif.ui.fyp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.robocook.razif.data.user.UserData
import kotlinx.coroutines.launch

class ForYouPageViewModel(private val userData: UserData) : ViewModel() {

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

}