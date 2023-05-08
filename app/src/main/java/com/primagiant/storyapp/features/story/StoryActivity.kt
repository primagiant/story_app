package com.primagiant.storyapp.features.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.primagiant.storyapp.data.local.datastore.AuthPreferences
import com.primagiant.storyapp.data.response.ListStoryItem
import com.primagiant.storyapp.databinding.ActivityStoryBinding
import com.primagiant.storyapp.features.MainViewModel
import com.primagiant.storyapp.features.MainViewModelFactory
import com.primagiant.storyapp.features.story.adapter.StoryListAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AuthPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.apply {
            isLoading.observe(this@StoryActivity) { isLoading ->
                showLoading(isLoading)
            }
            message.observe(this@StoryActivity) { msg ->
                Toast.makeText(this@StoryActivity, msg, Toast.LENGTH_SHORT).show()
            }
            getToken().observe(this@StoryActivity) { token ->
                getStoryList(token)
            }
        }
        binding.rvStoryList.apply {
            mainViewModel.storyList.observe(this@StoryActivity) { items ->
                val adapter = StoryListAdapter(items, this@StoryActivity)
                this.adapter = adapter
                adapter.setOnStoryListClickCallback(object :
                    StoryListAdapter.OnStoryListClickCallback {
                    override fun onItemClicked(story: ListStoryItem) {
                        val intent = Intent(this@StoryActivity, DetailStoryActivity::class.java)
                        intent.putExtra(DetailStoryActivity.ID, story.id)
                        startActivity(intent)
                    }
                })
            }
            layoutManager = LinearLayoutManager(this@StoryActivity)
        }

        /*binding.buttonLogout.setOnClickListener {
            authViewModel.logout()
            authViewModel.getToken().observe(this) { token ->
                isLogin(token)
            }
        }*/
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}