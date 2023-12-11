package com.example.robocook.razif.ui.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.fyp.ForYouPageActivity
import com.example.robocook.razif.ui.fyp.ForYouPageViewModel
import com.example.robocook.razif.ui.login.LoginViewModel
import com.example.robocook.razif.ui.register.RegisterViewModel

class ViewModelFactory(private val userData: UserData, private val context : Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userData) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userData) as T
            }

            modelClass.isAssignableFrom(ForYouPageViewModel::class.java) -> {
                ForYouPageViewModel(userData) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

        }

    }

}