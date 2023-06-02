package com.primagiant.storyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.primagiant.storyapp.data.local.preference.SettingPreferenceViewModel
import com.primagiant.storyapp.databinding.ActivityMainBinding
import com.primagiant.storyapp.features.SettingViewModelFactory
import com.primagiant.storyapp.features.auth.LoginFragment
import com.primagiant.storyapp.features.story.StoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val settingPreferenceViewModel: SettingPreferenceViewModel by viewModels {
        SettingViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreferenceViewModel.getToken().observe(this) { token ->
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