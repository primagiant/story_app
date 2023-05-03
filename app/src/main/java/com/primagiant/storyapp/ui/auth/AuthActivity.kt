package com.primagiant.storyapp.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.primagiant.storyapp.R
import com.primagiant.storyapp.databinding.ActivityAuthBinding

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
                RegisterFragment(),
                RegisterFragment::class.java.simpleName
            ).commit()
    }
}