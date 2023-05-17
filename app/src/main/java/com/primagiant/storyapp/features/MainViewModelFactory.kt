package com.primagiant.storyapp.features

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.primagiant.storyapp.data.local.repository.AuthRepository
import com.primagiant.storyapp.utils.AuthInjection

class MainViewModelFactory private constructor(
private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory()
{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null

        fun getInstance(context: Context): MainViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(AuthInjection.provideRepository(context))
            }.also { instance = it}
    }

}
