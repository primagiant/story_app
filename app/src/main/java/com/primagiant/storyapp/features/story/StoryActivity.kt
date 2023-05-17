package com.primagiant.storyapp.features.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.primagiant.storyapp.MainActivity
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.response.ListStoryItem
import com.primagiant.storyapp.databinding.ActivityStoryBinding
import com.primagiant.storyapp.features.MainViewModel
import com.primagiant.storyapp.features.MainViewModelFactory
import com.primagiant.storyapp.features.story.adapter.StoryListAdapter

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.apply {
            token.observe(this@StoryActivity) { token ->
                getStoryList(token)
            }
            isLoading.observe(this@StoryActivity) { isLoading ->
                showLoading(isLoading)
            }
            message.observe(this@StoryActivity) { msg ->
                Toast.makeText(this@StoryActivity, msg, Toast.LENGTH_SHORT).show()
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

        binding.addStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.logout -> {
                mainViewModel.logout()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                finish()
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