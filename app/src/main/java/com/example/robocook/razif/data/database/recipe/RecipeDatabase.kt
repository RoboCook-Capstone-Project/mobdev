package com.example.robocook.razif.data.database.recipe

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.robocook.razif.data.database.remotekeys.RemoteKeys
import com.example.robocook.razif.data.database.remotekeys.RemoteKeysDao

@Database(entities = [Recipe::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): RecipeDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java, "recipe_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it
                    }

            }

        }

    }

}