package com.primagiant.storyapp.di

import android.content.Context
import androidx.room.Database
import com.primagiant.storyapp.data.StoryRepository
import com.primagiant.storyapp.data.local.preference.SettingPreference
import com.primagiant.storyapp.data.local.room.MainDatabase
import com.primagiant.storyapp.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context) : StoryRepository {
        val database = MainDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }

    fun providePreferences(context: Context): SettingPreference {
        return SettingPreference.getInstance(context)
    }
}
