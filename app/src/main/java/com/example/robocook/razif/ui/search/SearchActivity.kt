package com.example.robocook.razif.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.robocook.R
import com.example.robocook.databinding.ActivitySearchBinding
import com.example.robocook.razif.data.response.RecipeList
import com.example.robocook.razif.data.user.UserData
import com.example.robocook.razif.ui.helper.ViewModelFactory
import com.example.robocook.razif.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var finalToken: String

    private fun displayNotFoundInfo(isNotFound: Boolean) {

        if(isNotFound){
            binding.tvNotFound.visibility = View.VISIBLE
        }
        else {
            binding.tvNotFound.visibility = View.GONE
        }

    }

    private fun putUserToAdapter(listOfUser: List<RecipeList>) {

        val listToPut = ArrayList<RecipeList>()
        for (user in listOfUser) {
            listToPut.add(user)
        }
        val adapter = SearchAdapter(listToPut)
        binding.rvRecipeSearch.adapter = adapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvRecipeSearch.setHasFixedSize(true)

        searchViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[SearchViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipeSearch.layoutManager = layoutManager

        searchViewModel.searchResult.observe(this) { searchResults ->
            if (searchResults != null) {
                displayNotFoundInfo(false)
                putUserToAdapter(searchResults)
            } else {
                displayNotFoundInfo(true)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.options_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.optSearchFeature).actionView as SearchView

        searchViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserData.getInstance(dataStore), this)
        )[SearchViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipeSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvRecipeSearch.addItemDecoration(itemDecoration)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search recipe here"
        searchViewModel.fetchToken().observe(this) { token ->
            if (token == "null") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                finalToken = "Bearer $token"
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(keyword: String?): Boolean {

                if (keyword != null) {
                    searchViewModel.searchForRecipe(finalToken, keyword)
                }
                searchView.clearFocus()
                return true

            }

            override fun onQueryTextChange(keyword: String?): Boolean {

                return false

            }
        })

        return true

    }

}