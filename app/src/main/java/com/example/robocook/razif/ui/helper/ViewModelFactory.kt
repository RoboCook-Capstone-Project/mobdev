package com.example.robocook.razif.ui.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.razif.data.database.helper.Injection
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.detail.DetailViewModel
import com.example.robocook.razif.ui.favorite.FavoriteRecipeViewModel
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
                @Suppress("UNCHECKED_CAST")
                return ForYouPageViewModel(userData, Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(userData) as T
            }

            modelClass.isAssignableFrom(FavoriteRecipeViewModel::class.java) -> {
                FavoriteRecipeViewModel(userData) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

        }

    }

}