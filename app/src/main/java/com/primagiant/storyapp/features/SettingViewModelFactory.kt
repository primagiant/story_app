package com.primagiant.storyapp.features

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.primagiant.storyapp.data.local.preference.SettingPreference
import com.primagiant.storyapp.data.local.preference.SettingPreferenceViewModel
import com.primagiant.storyapp.di.Injection

class SettingViewModelFactory(
    private val preference: SettingPreference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingPreferenceViewModel::class.java)) {
            return SettingPreferenceViewModel(preference) as T
        }
        throw IllegalArgumentException("Unknown view model class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: SettingViewModelFactory? = null

        fun getInstance(context: Context): SettingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SettingViewModelFactory(Injection.providePreferences(context))
            }.also { instance = it }
    }

}