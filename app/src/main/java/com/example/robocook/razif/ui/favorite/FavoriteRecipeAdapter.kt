package com.example.robocook.razif.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.robocook.R
import com.example.robocook.databinding.ItemFavoriteRecipeBinding
import com.example.robocook.razif.data.response.RecipeList

class FavoriteRecipeAdapter : RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>() {

    private var recipeList: List<RecipeList> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setRecipes(recipes: List<RecipeList>) {
        recipeList = recipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipeList.size

    inner class ViewHolder(private val binding: ItemFavoriteRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        private var isDetailsVisible = false

        init {
            binding.btShowDetail.setOnClickListener {
                isDetailsVisible = !isDetailsVisible
                showHideDetail()
                changeButtonImage()
            }
        }

        fun bind(data: RecipeList) {

            Glide.with(itemView.context).load(data.image_url).into(binding.ivRecipePicture)
            binding.tvRecipeTitle.text = data.title
            var recipeAuthor = data.author
            binding.tvRecipeAuthor.text = "by $recipeAuthor"
            binding.tvRecipeIngredients.text = giveNumbering(data.ingredients)
            binding.tvRecipeSteps.text = giveNumbering(data.steps)
            binding.tvIngredients.visibility = View.GONE
            binding.tvRecipeIngredients.visibility = View.GONE
            binding.tvSteps.visibility = View.GONE
            binding.tvRecipeSteps.visibility = View.GONE
            showHideDetail()
            changeButtonImage()

        }

        private fun showHideDetail() {

            val visibility = if (isDetailsVisible) View.VISIBLE else View.GONE
            binding.tvIngredients.visibility = visibility
            binding.tvRecipeIngredients.visibility = visibility
            binding.tvSteps.visibility = visibility
            binding.tvRecipeSteps.visibility = visibility

        }

        private fun changeButtonImage() {

            val arrowIconRes = if (isDetailsVisible) R.drawable.ic_less else R.drawable.ic_more
            binding.btShowDetail.setImageResource(arrowIconRes)

        }

        private fun giveNumbering(text: String): String {

            val lines = text.split("\n")
            val nonEmptyLines = lines.filter { it.isNotEmpty() }
            val numberedLines = nonEmptyLines.mapIndexed { index, line -> "${index + 1}. $line" }
            return numberedLines.joinToString("\n")

        }

    }
}