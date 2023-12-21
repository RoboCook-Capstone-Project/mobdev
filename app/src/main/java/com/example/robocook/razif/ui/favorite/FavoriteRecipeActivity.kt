package com.example.robocook.razif.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.robocook.R
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.fusion.FusionActivity
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class FavoriteRecipeActivity : AppCompatActivity() {

    private lateinit var favoriteRecipeViewModel: FavoriteRecipeViewModel
    private lateinit var recipeAdapter: FavoriteRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_recipe)

        supportActionBar?.title = "Favorite Recipes"

        favoriteRecipeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[FavoriteRecipeViewModel::class.java]

        recipeAdapter = FavoriteRecipeAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.rvRecipe)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recipeAdapter

        favoriteRecipeViewModel.fetchToken().observe(this) { token ->
            if (token == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                val finalToken = "Bearer $token"
                favoriteRecipeViewModel.fetchUserFavorites(finalToken)
            }
        }

        favoriteRecipeViewModel.recipeList.observe(this) { recipes ->
            recipeAdapter.setRecipes(recipes)
        }

        // fab ke halaman fussion
        val fabFusion: FloatingActionButton = findViewById(R.id.fab_fusion)
        fabFusion.setOnClickListener {
            val intent = Intent(this@FavoriteRecipeActivity, FusionActivity::class.java)
            startActivity(intent)
        }
    }
}