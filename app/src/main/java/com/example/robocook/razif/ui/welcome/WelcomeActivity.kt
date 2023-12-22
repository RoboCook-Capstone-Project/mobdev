package com.example.robocook.razif.ui.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.robocook.databinding.ActivityWelcomeBinding
import com.example.robocook.razif.ui.login.LoginActivity
import com.example.robocook.razif.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        supportActionBar?.hide()
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

    private fun setupAction() {
        binding.btRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        binding.btLogin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }
}