package com.example.robocook.razif.ui.fyp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.robocook.R
import com.example.robocook.databinding.ItemRecipeBinding
import com.example.robocook.razif.data.database.recipe.Recipe
import com.example.robocook.razif.ui.detail.DetailActivity

class RecipeAdapter() : PagingDataAdapter<Recipe, RecipeAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {

            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }

    class ViewHolder(private val binding: ItemRecipeBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Recipe) {

            Glide.with(this.itemView.context).load(data.image_url).into(binding.ivRecipePicture)
            binding.tvRecipeTitle.text = data.title
            binding.tvRecipeAuthor.text = data.author
            Log.d("Binder Recipe", data.image_url)

            this.itemView.setOnClickListener {
                val intentUserDetail = Intent(this.itemView.context, DetailActivity::class.java)
                intentUserDetail.putExtra("id", data.id)
                intentUserDetail.putExtra("image", data.image_url)
                intentUserDetail.putExtra("title", data.title)
                intentUserDetail.putExtra("author", data.author)
                intentUserDetail.putExtra("ingredients", data.ingredients)
                intentUserDetail.putExtra("steps", data.steps)
                this.itemView.context.startActivity(intentUserDetail)
            }

        }

    }

}