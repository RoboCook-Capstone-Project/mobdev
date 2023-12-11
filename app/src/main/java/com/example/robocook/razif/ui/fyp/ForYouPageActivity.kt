package com.example.robocook.razif.ui.fyp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.R
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")
class ForYouPageActivity : AppCompatActivity() {
    private lateinit var forYouPageViewModel: ForYouPageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_you_page)
        setupView()
        setupViewModel()
    }
    private fun setupView() {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

    }

    private fun setupViewModel() {

        forYouPageViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[ForYouPageViewModel::class.java]

    }

    override fun onResume() {

        super.onResume()
        hasLogin()

    }
    private fun hasLogin() {

        forYouPageViewModel.fetchToken().observe(this) {

            if (it == "null") {

                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.optLogout -> forYouPageViewModel.userLogout()
        }

        return super.onOptionsItemSelected(item)

    }

}