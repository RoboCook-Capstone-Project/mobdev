package com.example.robocook.razif.ui.fusion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.robocook.R
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.favorite.FavoriteRecipeAdapter
import com.example.robocook.razif.ui.favorite.FavoriteRecipeViewModel
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class FusionResultActivity : AppCompatActivity() {

    private lateinit var fusionResultViewModel: FusionResultViewModel
    private lateinit var fusionRecipeAdapter: FusionRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fusion_result)

        supportActionBar?.title = "Fusion Result"

        fusionResultViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[FusionResultViewModel::class.java]

        fusionRecipeAdapter = FusionRecipeAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.rvRecipe)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fusionRecipeAdapter

        val firstChoice = intent.getIntExtra("firstChoice", -1)
        val secondChoice = intent.getIntExtra("secondChoice", -1)

        fusionResultViewModel.fetchToken().observe(this) { token ->
            if (token == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                val finalToken = "Bearer $token"
                fusionResultViewModel.fetchFusionResult(finalToken, firstChoice, secondChoice)
            }
        }

        fusionResultViewModel.recipeList.observe(this) { recipes ->
            fusionRecipeAdapter.setRecipes(recipes)
        }

    }
}