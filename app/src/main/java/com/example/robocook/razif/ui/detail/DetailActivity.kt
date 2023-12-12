package com.example.robocook.razif.ui.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.robocook.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        tvIngredients.text = intentIngredients

        val intentSteps = intent.getStringExtra("steps")
        val tvSteps: TextView = binding.tvDetailRecipeSteps
        tvSteps.text = intentSteps

        playAnimation()

    }

    private fun playAnimation() {

        val ivRecipePicture = ObjectAnimator.ofFloat(binding.ivDetailRecipeImage, View.ALPHA, 1f).setDuration(800)
        val tvRecipeTitle = ObjectAnimator.ofFloat(binding.tvDetailRecipeTitle, View.ALPHA, 1f).setDuration(800)
        val tvRecipeAuthor = ObjectAnimator.ofFloat(binding.tvDetailRecipeAuthor, View.ALPHA, 1f).setDuration(800)
        val tvRecipeIngredients = ObjectAnimator.ofFloat(binding.tvDetailRecipeIngredients, View.ALPHA, 1f).setDuration(800)
        val tvRecipeSteps = ObjectAnimator.ofFloat(binding.tvDetailRecipeSteps, View.ALPHA, 1f).setDuration(800)


        AnimatorSet().apply {
            playSequentially(
                ivRecipePicture,
                tvRecipeTitle,
                tvRecipeAuthor,
                tvRecipeIngredients,
                tvRecipeSteps
            )
            start()
        }

    }

}