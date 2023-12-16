package com.example.robocook.razif.ui.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.robocook.databinding.ActivityDetailBinding
import com.example.robocook.razif.data.response.AddToFavoriteResponse
import com.example.robocook.razif.data.retrofit.ApiConfig
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var recipeId by Delegates.notNull<Int>()
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentId = intent.getIntExtra("id", -1)
        recipeId = intentId

        val intentPicture = intent.getStringExtra("image")
        val ivPicture: ImageView = binding.ivDetailRecipeImage
        Glide.with(this)
            .load(intentPicture)
            .into(ivPicture)

        val intentTitle = intent.getStringExtra("title")
        val tvTitle: TextView = binding.tvDetailRecipeTitle
        tvTitle.text = intentTitle

        val intentAuthor = intent.getStringExtra("author")
        val tvAuthor: TextView = binding.tvDetailRecipeAuthor
        tvAuthor.text = intentAuthor

        val intentIngredients = intent.getStringExtra("ingredients")
        val tvIngredients: TextView = binding.tvDetailRecipeIngredients
        tvIngredients.text = formatNumberedText(intentIngredients)

        val intentSteps = intent.getStringExtra("steps")
        val tvSteps: TextView = binding.tvDetailRecipeSteps
        tvSteps.text = formatNumberedText(intentSteps)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[DetailViewModel::class.java]

        playAnimation()

        binding.btFavorite.setOnClickListener {
            addToFavorites()
        }

    }

    private fun formatNumberedText(text: String?): String {
        if (text.isNullOrBlank()) return ""

        val lines = text.split("\n")
        val nonEmptyLines = lines.filter { it.isNotEmpty() }
        val numberedLines = nonEmptyLines.mapIndexed { index, line -> "${index + 1}. $line" }
        return numberedLines.joinToString("\n")
    }

    private fun playAnimation() {

        val btFavorite = ObjectAnimator.ofFloat(binding.btFavorite, View.ALPHA, 1f).setDuration(1000)
        val ivRecipePicture = ObjectAnimator.ofFloat(binding.ivDetailRecipeImage, View.ALPHA, 1f).setDuration(1000)
        val tvRecipeTitle = ObjectAnimator.ofFloat(binding.tvDetailRecipeTitle, View.ALPHA, 1f).setDuration(1000)
        val tvRecipeAuthor = ObjectAnimator.ofFloat(binding.tvDetailRecipeAuthor, View.ALPHA, 1f).setDuration(1000)
        val tvIngredients = ObjectAnimator.ofFloat(binding.tvIngredients, View.ALPHA, 1f).setDuration(1000)
        val tvRecipeIngredients = ObjectAnimator.ofFloat(binding.tvDetailRecipeIngredients, View.ALPHA, 1f).setDuration(1000)
        val tvSteps = ObjectAnimator.ofFloat(binding.tvSteps, View.ALPHA, 1f).setDuration(1000)
        val tvRecipeSteps = ObjectAnimator.ofFloat(binding.tvDetailRecipeSteps, View.ALPHA, 1f).setDuration(1000)


        AnimatorSet().apply {
            playTogether(
                btFavorite,
                ivRecipePicture,
                tvRecipeTitle,
                tvRecipeAuthor,
                tvIngredients,
                tvRecipeIngredients,
                tvSteps,
                tvRecipeSteps
            )
            start()
        }

    }

    private fun addToFavorites() {
        val token = "Bearer " + getToken()
        if (token.isNotEmpty()) {
            val api = ApiConfig.getApiService()
            val client = api.addRecipeToFavorite(token, recipeId)
            client.enqueue(object : Callback<AddToFavoriteResponse> {
                override fun onResponse(
                    call: Call<AddToFavoriteResponse>,
                    response: Response<AddToFavoriteResponse>
                ) {
                    if (response.isSuccessful) {
                        val addToFavoriteResponse = response.body()
                        if (addToFavoriteResponse != null && !addToFavoriteResponse.error) {
                            Toast.makeText(
                                this@DetailActivity,
                                "Added to your favorite!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Add to favorite token", token)
                            Log.d("Add to favorite id", recipeId.toString())
                        } else {
                            Toast.makeText(
                                this@DetailActivity,
                                "Failed to add to favorite",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailActivity,
                            "HTTP error: " + response.code(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AddToFavoriteResponse>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity,
                        "Network error: " + t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(this, "Missing token for authentication", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getToken(): String {
        var token = ""
        detailViewModel.fetchToken().observe(this) {
            if (it == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                token = it
            }

        }
        return token
    }

}