package com.primagiant.storyapp.features

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.primagiant.storyapp.MainActivity
import com.primagiant.storyapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class SplashActivity : AppCompatActivity() {

    private val scope = CoroutineScope(newSingleThreadContext("splash"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        scope.launch {
            nextActivity()
        }
    }

    private suspend fun nextActivity() {
        delay(3000)
        val toMainActivity = Intent(this, MainActivity::class.java)
        startActivity(toMainActivity)
        finish()
    }

}