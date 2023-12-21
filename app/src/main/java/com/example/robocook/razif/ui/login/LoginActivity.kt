package com.example.robocook.razif.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.databinding.ActivityLoginBinding
import com.example.robocook.razif.data.response.LoginResponse
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.fyp.ForYouPageActivity
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    companion object {
        private var validation: Boolean? = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()
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

        supportActionBar?.hide()
    }

    private fun setupViewModel() {

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[LoginViewModel::class.java]

    }

    private fun setupAction() {

        binding.btLogin.setOnClickListener {

            val tvEmail = binding.etEmailLogin.text.toString()
            val tvPassword = binding.etPasswordLogin.text.toString()

            when {
                tvEmail.isEmpty() -> { binding.loEtEmailLogin.error = "Input your email" }
                tvPassword.isEmpty() -> { binding.loEtPasswordLogin.error = "Input your password" }

                else -> {
                    loginViewModel.userLogin(tvEmail, tvPassword)
                    loginViewModel.loginResponse.observe(this) { loginRespond -> validationCheck(loginRespond) }
                    loginViewModel.isLoadingLogin.observe(this) { showLoadingLogin(it) }
                }
            }
        }

        binding.btRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validationCheck(loginResponse: LoginResponse?) {

        val status = loginResponse?.error
        validation = status

        if (status == false) {
            AlertDialog.Builder(this).apply {
                setTitle("Success!")
                setMessage("Your login is successful.")
                setPositiveButton("Continue") { _, _ ->
                    val intent = Intent(context, ForYouPageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Failed!")
                setMessage("Your login has failed.")
                setPositiveButton("Try again") { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun showLoadingLogin(isLoadingLogin: Boolean) {
        binding.pbLogin.visibility = if (isLoadingLogin) View.VISIBLE else View.GONE
    }

}