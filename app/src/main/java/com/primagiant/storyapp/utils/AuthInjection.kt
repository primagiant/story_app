package com.primagiant.storyapp.utils

import android.content.Context
import com.primagiant.storyapp.data.local.repository.AuthRepository

object AuthInjection {
    fun provideRepository(context: Context): AuthRepository {
        return AuthRepository.getInstance(context)
    }
}
