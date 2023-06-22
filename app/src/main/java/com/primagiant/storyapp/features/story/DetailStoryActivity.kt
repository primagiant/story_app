package com.primagiant.storyapp.features.story

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.preference.SettingPreferenceViewModel
import com.primagiant.storyapp.databinding.ActivityDetailStoryBinding
import com.primagiant.storyapp.utils.SettingViewModelFactory
import com.primagiant.storyapp.utils.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val settingPreferenceViewModel: SettingPreferenceViewModel by viewModels {
        SettingViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story_title)

        storyViewModel.apply {
            isLoading.observe(this@DetailStoryActivity) { isLoading ->
                showLoading(isLoading)
            }
            message.observe(this@DetailStoryActivity) { msg ->
                Toast.makeText(this@DetailStoryActivity, msg, Toast.LENGTH_SHORT).show()
            }
            val idStory = intent?.getStringExtra(EXTRA_ID)
            if (idStory != null) {
                getDetailStory(idStory)
            }
        }

        storyViewModel.detailStory.observe(this) { item ->
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
        const val EXTRA_ID = "1"
        const val EXTRA_STORY_ITEMS = "extra_story"
    }

}
