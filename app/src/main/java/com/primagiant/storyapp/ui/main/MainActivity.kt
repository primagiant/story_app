package com.primagiant.storyapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.primagiant.storyapp.R
import com.primagiant.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}