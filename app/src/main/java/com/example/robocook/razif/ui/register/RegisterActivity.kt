package com.example.robocook.razif.ui.register

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
import com.example.robocook.databinding.ActivityRegisterBinding
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")
class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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

        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[RegisterViewModel::class.java]

    }

    private fun setupAction() {

        binding.btRegister.setOnClickListener {

            val tvEmail = binding.etEmailSignup.text.toString()
            val tvName = binding.etNameSignup.text.toString()
            val tvPassword = binding.etPasswordSignup.text.toString()

            when {
                tvName.isEmpty() -> { binding.loEtNameSignup.error = "Input your name" }
                tvEmail.isEmpty() -> { binding.loEtEmailSignup.error = "Create your email" }
                tvPassword.isEmpty() -> { binding.loEtPasswordSignup.error = "Create your password" }

                else -> {
                    registerViewModel.userRegister(tvEmail, tvName, tvPassword)
                    registerViewModel.isRegistered.observe(this) { validationCheck(it) }
                    registerViewModel.isLoadingRegister.observe(this) { showLoadingRegister(it) }
                }
            }
        }

        binding.btLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validationCheck(isValid: Boolean) {

        if (isValid) {
            AlertDialog.Builder(this).apply {
                setTitle("Success!")
                setMessage("Your account is ready. Login to see recipe inspiration.")
                setPositiveButton("Next") { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
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
                setMessage("That account is already registered. Create a new one or use the existing one.")
                setPositiveButton("Try again") { _, _ ->
                    val intent = Intent(context, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun showLoadingRegister(isLoadingRegister: Boolean) {
        binding.pbRegister.visibility = if (isLoadingRegister) View.VISIBLE else View.GONE
    }
}