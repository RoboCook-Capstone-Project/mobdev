package com.example.robocook.razif.ui.toasty

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.robocook.R
import com.example.robocook.databinding.ActivityToastyBinding
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")
class ToastyActivity : AppCompatActivity() {

    private lateinit var toastyViewModel: ToastyViewModel
    private lateinit var binding: ActivityToastyBinding

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToastyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toastyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        ) [ToastyViewModel::class.java]

        toastyViewModel.fetchToken().observe(this) { token ->
            if (token == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                val finalToken = "Bearer $token"
                toastyViewModel.feelingToasty(finalToken)
            }
        }

        toastyViewModel.toasty.observe(this) { toasty ->
            binding.apply {
                Glide.with(applicationContext)
                    .load(toasty.image_url)
                    .into(imageToasty)
                tvTitle.text = toasty.title
                tvAuthor.text = getString(R.string.author_format, toasty.author)
                tvIngredientsFill.text = giveNumbering(toasty.ingredients)
                tvStepsFill.text = giveNumbering(toasty.steps)
            }
        }
    }

    private fun giveNumbering(text: String): String {

        val lines = text.split("\n")
        val nonEmptyLines = lines.filter { it.isNotEmpty() }
        val numberedLines = nonEmptyLines.mapIndexed { index, line -> "${index + 1}. $line" }
        return numberedLines.joinToString("\n")

    }
}