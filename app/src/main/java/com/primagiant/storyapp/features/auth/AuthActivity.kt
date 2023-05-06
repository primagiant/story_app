package com.primagiant.storyapp.features.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.primagiant.storyapp.R
import com.primagiant.storyapp.databinding.ActivityAuthBinding
import com.primagiant.storyapp.features.auth.login.LoginFragment

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragmentManager = supportFragmentManager

        fragmentManager
            .beginTransaction()
            .add(
                R.id.auth_container,
                LoginFragment(),
                LoginFragment::class.java.simpleName
            ).commit()
    }
}