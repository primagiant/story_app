package com.primagiant.storyapp.features.story

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.datastore.AuthPreferences
import com.primagiant.storyapp.databinding.ActivityDetailStoryBinding
import com.primagiant.storyapp.features.MainViewModel
import com.primagiant.storyapp.features.MainViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story_title)

        val pref = AuthPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.apply {
            isLoading.observe(this@DetailStoryActivity) { isLoading ->
                showLoading(isLoading)
            }
            message.observe(this@DetailStoryActivity) { msg ->
                Toast.makeText(this@DetailStoryActivity, msg, Toast.LENGTH_SHORT).show()
            }
            getToken().observe(this@DetailStoryActivity) { token ->
                val idStory = intent?.getStringExtra(ID)
                if (idStory != null) {
                    getDetailStory(idStory, token)
                }
            }
        }

        mainViewModel.detailStory.observe(this) { item ->
            binding.apply {
                detailAuthor.text = item.story.name
                detailDesc.text = item.story.description
                Glide.with(this@DetailStoryActivity)
                    .load(item.story.photoUrl)
                    .into(detailImage)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ID = "1"
    }

}
