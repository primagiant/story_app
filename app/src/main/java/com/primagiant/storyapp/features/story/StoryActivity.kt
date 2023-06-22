package com.primagiant.storyapp.features.story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.primagiant.storyapp.MainActivity
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.preference.SettingPreferenceViewModel
import com.primagiant.storyapp.databinding.ActivityStoryBinding
import com.primagiant.storyapp.features.maps.MapsActivity
import com.primagiant.storyapp.features.story.adapter.LoadingStateAdapter
import com.primagiant.storyapp.features.story.adapter.StoryListAdapter
import com.primagiant.storyapp.utils.SettingViewModelFactory
import com.primagiant.storyapp.utils.ViewModelFactory
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding

    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val settingPreferenceViewModel: SettingPreferenceViewModel by viewModels {
        SettingViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycleView()

        binding.addStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager

        binding.rvStoryList.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStoryList.addItemDecoration(itemDecoration)

        val storyAdapter = StoryListAdapter()
        binding.rvStoryList.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        lifecycleScope.launch {
            settingPreferenceViewModel.getToken().observe(this@StoryActivity) { token ->
                storyViewModel.listStory(token).observe(this@StoryActivity) { pagingData ->
                    storyAdapter.submitData(lifecycle, pagingData)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                settingPreferenceViewModel.deleteToken()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                finish()
                startActivity(intent)
                true
            }

            R.id.setting -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }

            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}