package com.example.robocook.razif.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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


    private fun setupAction() {

        binding.btRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        binding.btLogin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }

    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.ivBannerWelcome, View.TRANSLATION_X, -30f, 30f).apply {

            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            duration = 5000

        }.start()

        val tvTitle = ObjectAnimator.ofFloat(binding.tvWelcomeTitle, View.ALPHA, 1f).setDuration(800)
        val tvDescription = ObjectAnimator.ofFloat(binding.tvWelcomeDescription, View.ALPHA, 1f).setDuration(800)
        val btSignup = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f).setDuration(800)
        val btLogin = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f).setDuration(800)

        val btTogether = AnimatorSet().apply {
            playTogether(btSignup, btLogin)
        }

        AnimatorSet().apply {
            playSequentially(tvTitle, tvDescription, btTogether)
            start()
        }

    }

}