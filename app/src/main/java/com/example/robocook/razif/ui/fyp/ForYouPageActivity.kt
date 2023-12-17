package com.example.robocook.razif.ui.fyp

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.robocook.MainActivity
import com.example.robocook.R
import com.example.robocook.databinding.ActivityForYouPageBinding
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.add_recipe.AddRecipeActivity
import com.example.robocook.razif.ui.favorite.FavoriteRecipeActivity
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.search.SearchActivity
import com.example.robocook.razif.ui.toasty.ToastActivity
import com.example.robocook.razif.ui.welcome.WelcomeActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class ForYouPageActivity : AppCompatActivity() {

    private lateinit var forYouPageViewModel: ForYouPageViewModel

    private lateinit var adapter: RecipeAdapter

    private lateinit var binding: ActivityForYouPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityForYouPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipe.layoutManager = layoutManager

        binding.rvRecipe.addItemDecoration(object :
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            }
        })

        // fab ke halaman add recipe
        binding.fabAddRecipe.setOnClickListener {
            val intent = Intent(this@ForYouPageActivity, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        setupView()
        setupViewModel()
        hasLogin()
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

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipe.layoutManager = layoutManager
        adapter = RecipeAdapter()
        binding.rvRecipe.adapter = adapter

    }

    private fun setupViewModel() {

        forYouPageViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[ForYouPageViewModel::class.java]

    }

    private fun setupAction(token: String) {

        forYouPageViewModel.fetchRecipePaging(token).observe(this) {
            binding.loSwipeRefresh.isRefreshing = false
            adapter.submitData(lifecycle, it)
        }

    }

    private fun setupSwipeRefresh() {

        binding.loSwipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
//            R.id.optFusion -> startActivity(Intent(this, MainActivity::class.java))
            R.id.optToasty -> startActivity(Intent(this, ToastActivity::class.java))
            R.id.optSearch -> startActivity(Intent(this, SearchActivity::class.java))
//            R.id.optAdd -> startActivity(Intent(this, MainActivity::class.java))
            R.id.optFavorite -> startActivity(Intent(this, FavoriteRecipeActivity::class.java))
            R.id.optLogout -> forYouPageViewModel.userLogout()
        }

        return super.onOptionsItemSelected(item)

    }

    private fun hasLogin() {

        forYouPageViewModel.fetchToken().observe(this) {

            if (it == "null") {

                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

            } else {

                setupAction("Bearer $it")
                setupSwipeRefresh()

            }

        }

        forYouPageViewModel.isLoadingMain.observe(this) { showLoadingMain(it) }

    }

    override fun onResume() {

        super.onResume()
        hasLogin()

    }

    private fun showLoadingMain(isLoadingMain: Boolean) {

        binding.pb.visibility = if (isLoadingMain) View.VISIBLE else View.GONE

    }

}