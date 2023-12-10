package com.example.robocook.razif.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserData private constructor(private val dataStore: DataStore<Preferences>){

    private val TOKEN_KEY = stringPreferencesKey("token")

    companion object {

        @Volatile
        private var INSTANCE: UserData? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserData {

            return INSTANCE ?: synchronized(this) {
                val instance = UserData(dataStore)
                INSTANCE = instance
                instance
            }

        }

    }

    fun fetchUserToken(): Flow<String> {

        return dataStore.data.map {
            it[TOKEN_KEY] ?: "null"
        }

    }

    suspend fun saveUserToken(token: String) {

        dataStore.edit {
            it[this.TOKEN_KEY] = token
        }

    }

    suspend fun userLogout() {

        dataStore.edit {
            it[this.TOKEN_KEY] = "null"
        }

    }

}