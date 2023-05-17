package com.primagiant.storyapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.primagiant.storyapp.databinding.ActivityMainBinding
import com.primagiant.storyapp.features.MainViewModel
import com.primagiant.storyapp.features.MainViewModelFactory
import com.primagiant.storyapp.features.auth.LoginFragment
import com.primagiant.storyapp.features.story.StoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.token.observe(this) { token ->
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