package com.example.robocook.razif.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.robocook.R
import com.example.robocook.razif.data.response.RecipeList
import com.example.robocook.razif.ui.detail.DetailActivity

class SearchAdapter(private val listRecipe: List<RecipeList>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    companion object{
        const val KEYWORD = "mie"
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_search, viewGroup, false))

    override fun getItemCount() = listRecipe.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Glide.with(viewHolder.itemView.context)
            .load(listRecipe[position].image_url)
            .into(viewHolder.ivPictureRecipeSearch)
        viewHolder.tvNameRecipeSearch.text = listRecipe[position].title

        viewHolder.itemView.setOnClickListener {
            val intentDetail = Intent(viewHolder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("id", listRecipe[viewHolder.adapterPosition].id)
            intentDetail.putExtra("image", listRecipe[viewHolder.adapterPosition].image_url)
            intentDetail.putExtra("title", listRecipe[viewHolder.adapterPosition].title)
            intentDetail.putExtra("author", listRecipe[viewHolder.adapterPosition].author)
            intentDetail.putExtra("ingredients", listRecipe[viewHolder.adapterPosition].ingredients)
            intentDetail.putExtra("steps", listRecipe[viewHolder.adapterPosition].steps)
            viewHolder.itemView.context.startActivity(intentDetail)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPictureRecipeSearch: ImageView = view.findViewById(R.id.ivPictureRecipeSearch)
        val tvNameRecipeSearch: TextView = view.findViewById(R.id.tvNameSearch)

    }

}