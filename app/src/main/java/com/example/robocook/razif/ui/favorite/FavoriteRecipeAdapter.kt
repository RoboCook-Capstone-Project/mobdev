package com.example.robocook.razif.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.robocook.databinding.ItemRecipeBinding
import com.example.robocook.razif.data.response.RecipeList

class FavoriteRecipeAdapter : RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>() {

    private var recipeList: List<RecipeList> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setRecipes(recipes: List<RecipeList>) {
        recipeList = recipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipeList.size

    inner class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RecipeList) {

            Glide.with(itemView.context).load(data.image_url).into(binding.ivRecipePicture)
            binding.tvRecipeTitle.text = data.title
            var recipeAuthor = data.author
            binding.tvRecipeAuthor.text = "by $recipeAuthor"
        }
    }
}
