package com.example.robocook.razif.ui.add_recipe

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.robocook.R
import com.example.robocook.databinding.ActivityAddRecipeBinding
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.fyp.ForYouPageActivity
import com.example.robocook.razif.ui.fyp.ForYouPageViewModel
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.helper.createCustomTempFile
import com.example.robocook.razif.ui.helper.reduceFileImage
import com.example.robocook.razif.ui.helper.uriToFile
import com.example.robocook.razif.ui.welcome.WelcomeActivity
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var forYouPageViewModel: ForYouPageViewModel
    private val addRecipeViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[AddRecipeViewModel::class.java]
    }

    private var getFile: File? = null
    private lateinit var currentPhoto: String

    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
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
        forYouPageViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[ForYouPageViewModel::class.java]
    }

    private fun setupCamera() {
        binding.btCameraInput.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createCustomTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@AddRecipeActivity,
                    "com.example.robocook",
                    it
                )
                currentPhoto = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhoto)
            myFile.let {file ->
                getFile = reduceFileImage(myFile)
                binding.ivNewRecipePicture.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun setupGallery() {
        binding.btGalleryInput.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Pick a picture")
            launcherIntentGallery.launch(chooser)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage = result.data?.data as Uri
            selectedImage.let { uri ->
                val myFile = uriToFile(uri, this@AddRecipeActivity)
                getFile = reduceFileImage(myFile)
                binding.ivNewRecipePicture.setImageURI(uri)
            }
        }
    }

    private fun setupUpload() {
        binding.btCreateNewRecipe.setOnClickListener {
            forYouPageViewModel.fetchToken().observe(this) {
                val tvTitle = binding.etTitleRecipe.text.toString()
                val tvIngredients = binding.etIngredientsRecipe.text.toString()
                val tvSteps = binding.etStepsRecipe.text.toString()
                if (getFile != null && tvTitle.isNotBlank() && tvIngredients.isNotBlank() && tvSteps.isNotBlank()) {
                    Log.d("UploadNewRecipe", "Title: $tvTitle, Ingredients: $tvIngredients, Steps: $tvSteps")
                    addRecipeViewModel.createNewRecipe("Bearer $it", tvTitle, getFile as File, tvIngredients, tvSteps)
                    addRecipeViewModel.addRecipeResponse.observe(this) {
                        Toast.makeText(this, "Your recipe has been shared!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ForYouPageActivity::class.java))
                    }
                } else {
                    Toast.makeText(this, "All field should be filled!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hasLogin() {
        forYouPageViewModel.fetchToken().observe(this) {
            if (it == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                setupCamera()
                setupGallery()
                setupUpload()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hasLogin()
    }

}