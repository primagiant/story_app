package com.primagiant.storyapp.data.local.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

const val DATASTORE_NAME = "AUTH"
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class AuthRepository(
    private val context: Context
) {

    suspend fun saveToken(token: String) {
        context.datastore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getToken() = context.datastore.data.map { preferences ->
        preferences[TOKEN] ?: ""
    }

    companion object {
        val TOKEN = stringPreferencesKey("token")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(context: Context): AuthRepository =
            instance ?: synchronized(this) {
            instance ?: AuthRepository(context)
        }.also { instance = it }
    }
}