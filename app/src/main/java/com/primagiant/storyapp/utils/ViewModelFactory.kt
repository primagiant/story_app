package com.primagiant.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.primagiant.storyapp.data.StoryRepository
import com.primagiant.storyapp.data.local.preference.SettingPreference
import com.primagiant.storyapp.di.Injection
import com.primagiant.storyapp.features.story.StoryViewModel

class ViewModelFactory(
    private val repository: StoryRepository,
    private val pref: SettingPreference,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(repository, pref) as T
        }
        throw IllegalArgumentException("Unknown view model class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), Injection.providePreferences(context))
            }.also {  }
    }

}