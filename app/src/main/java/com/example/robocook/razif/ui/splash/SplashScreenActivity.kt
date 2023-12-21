package com.example.robocook.razif.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.R
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.fyp.ForYouPageActivity
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        setupViewModel()

        val splashTime = 1000L

        Handler(Looper.getMainLooper()).postDelayed({
            var intent = Intent(this, ForYouPageActivity::class.java)
            splashScreenViewModel.fetchToken().observe(this) { token ->
                if (token == "null") {
                    intent = Intent(this, WelcomeActivity::class.java)
                }
            }

            startActivity(intent)
            finish()
        }, splashTime)
    }

//    private fun hasLogin() {
//        splashScreenViewModel.fetchToken().observe(this) { token ->
//            if (token == null)
//        }
//    }

    private fun setupViewModel() {
        splashScreenViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[SplashScreenViewModel::class.java]
    }
}