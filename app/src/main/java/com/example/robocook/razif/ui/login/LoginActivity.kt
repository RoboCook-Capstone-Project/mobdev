package com.example.robocook.razif.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.example.robocook.MainActivity
import com.example.robocook.R
import com.example.robocook.databinding.ActivityLoginBinding
import com.example.robocook.razif.data.response.LoginResponse
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.fyp.ForYouPageActivity
import com.example.robocook.razif.ui.fyp.ForYouPageViewModel
import com.example.robocook.razif.ui.helper.ViewModelFactory

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
        playAnimation()
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
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.ivBannerLogin, View.TRANSLATION_X, -30f, 30f).apply {

            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            duration = 2500

        }.start()

        val tvEmailElement = ObjectAnimator.ofFloat(binding.tvEmailLogin, View.ALPHA, 1f).setDuration(1000)
        val tvEmail = ObjectAnimator.ofFloat(binding.loEtEmailLogin, View.ALPHA, 1f).setDuration(1000)
        val tvPasswordElement = ObjectAnimator.ofFloat(binding.tvPasswordLogin, View.ALPHA, 1f).setDuration(400)
        val tvPassword = ObjectAnimator.ofFloat(binding.loEtPasswordLogin, View.ALPHA, 1f).setDuration(400)
        val btLogin = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f).setDuration(400)

        val emailTogether = AnimatorSet().apply {
            playTogether(tvEmailElement, tvEmail)
        }

        val passwordTogether = AnimatorSet().apply {
            playTogether(tvPasswordElement, tvPassword)
        }

        AnimatorSet().apply {
            playSequentially(
                emailTogether,
                passwordTogether,
                btLogin
            )
            start()
        }

    }


    private fun validationCheck(loginResponse: LoginResponse?) {

        var status = loginResponse?.error
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