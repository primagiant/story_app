package com.primagiant.storyapp.features.story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.primagiant.storyapp.MainActivity
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.datastore.AuthPreferences
import com.primagiant.storyapp.databinding.ActivityMainBinding
import com.primagiant.storyapp.databinding.ActivityStoryBinding
import com.primagiant.storyapp.features.auth.AuthViewModel
import com.primagiant.storyapp.features.auth.AuthViewModelFactory
import com.primagiant.storyapp.features.auth.login.LoginFragment

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AuthPreferences.getInstance(dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]

        binding.buttonLogout.setOnClickListener {
            authViewModel.logout()
            authViewModel.getToken().observe(this) { token ->
                isLogin(token)
            }
        }
    }

    private fun isLogin(token: String) {
        if (token != "") {
            val intent = Intent(this@StoryActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}