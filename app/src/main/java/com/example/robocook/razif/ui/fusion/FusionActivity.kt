package com.example.robocook.razif.ui.fusion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.R
import com.example.robocook.databinding.ActivityFusionBinding
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.favorite.FavoriteRecipeViewModel
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class FusionActivity : AppCompatActivity() {

    private lateinit var fusionViewModel: FusionViewModel
    private var firstChoice: Int = -1
    private var secondChoice: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fusion)

        fusionViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[FusionViewModel::class.java]

        val dynamicRadioGroup1: RadioGroup = findViewById(R.id.dynamicRadioGroup1)
        val dynamicRadioGroup2: RadioGroup = findViewById(R.id.dynamicRadioGroup2)
        val fusionButton: Button = findViewById(R.id.btFusion)

        fusionViewModel.fetchToken().observe(this) { token ->
            if (token == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                val finalToken = "Bearer $token"
                fusionViewModel.fetchUserFavorites(finalToken)
            }
        }

        fusionViewModel.favoriteRecipes.observe(this) { recipeList ->

            dynamicRadioGroup1.removeAllViews()
            dynamicRadioGroup2.removeAllViews()

            if (recipeList.isNotEmpty()) {
                for (recipe in recipeList) {
                    val radioButton1 = RadioButton(this)
                    radioButton1.text = recipe.title
                    radioButton1.tag = recipe.id
                    dynamicRadioGroup1.addView(radioButton1)

                    val radioButton2 = RadioButton(this)
                    radioButton2.text = recipe.title
                    radioButton2.tag = recipe.id
                    dynamicRadioGroup2.addView(radioButton2)
                }
            } else {
                Log.d("FusionActivity", "Recipe list is empty.")
            }
        }

        dynamicRadioGroup1.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val selectedId = radioButton.tag as Int
            firstChoice = selectedId
        }

        dynamicRadioGroup2.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val selectedId = radioButton.tag as Int
            secondChoice = selectedId
        }

        fusionButton.setOnClickListener {
            if (firstChoice == -1 || secondChoice == -1) {
                Toast.makeText(this, "Please pick one from both choices", Toast.LENGTH_SHORT).show()
            } else if (firstChoice == secondChoice) {
                Toast.makeText(this, "You can't pick the same recipe for both", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, FusionResultActivity::class.java)
                intent.putExtra("firstChoice", firstChoice)
                intent.putExtra("secondChoice", secondChoice)
                startActivity(intent)
            }
        }

    }
}