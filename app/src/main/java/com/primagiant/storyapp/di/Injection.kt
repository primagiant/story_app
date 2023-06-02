package com.primagiant.storyapp.di

import android.content.Context
import com.primagiant.storyapp.data.local.preference.SettingPreference

object Injection {

    fun provideRepository(context: Context) {
        // TODO (Need to Implement)
    }

    fun providePreferences(context: Context): SettingPreference {
        return SettingPreference.getInstance(context)
    }
}
