package com.primagiant.storyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.primagiant.storyapp.data.local.datastore.AuthPreferences
import com.primagiant.storyapp.databinding.ActivityMainBinding
import com.primagiant.storyapp.features.auth.AuthViewModel
import com.primagiant.storyapp.features.auth.AuthViewModelFactory
import com.primagiant.storyapp.features.auth.login.LoginFragment
import com.primagiant.storyapp.features.story.StoryActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AuthPreferences.getInstance(dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]

        authViewModel.getToken().observe(this) { token ->
            isLogin(token)
        }

    }

    private fun isLogin(token: String) {
        val fragmentManager = supportFragmentManager
        if (token != "") {
            // is login
            val intent = Intent(this@MainActivity, StoryActivity::class.java)
            startActivity(intent)
        } else {
            // is not Login
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    LoginFragment(),
                    LoginFragment::class.java.simpleName
                ).commit()
        }
    }
}