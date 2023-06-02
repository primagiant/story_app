package com.primagiant.storyapp.features.story

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primagiant.storyapp.databinding.ActivityStoryBinding
import com.primagiant.storyapp.features.story.adapter.StoryListAdapter

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding

    private lateinit var storyAdapter: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getToken()
//
//        setupRecycleView()

        /* binding.rvStoryList.apply {
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
        } */

//        binding.addStory.setOnClickListener {
//            val intent = Intent(this, AddStoryActivity::class.java)
//            startActivity(intent)
//        }
    }

//    private fun setupRecycleView() {
//        storyAdapter = StoryListAdapter()
//        binding.rvStoryList.adapter = storyAdapter
//        lifecycleScope.launch {
//            mainViewModel.listData.collect { pagingData ->
//                storyAdapter.submitData(pagingData)
//            }
//        }
//    }
//
//    private fun getToken() {
//        mainViewModel.apply {
//            token.observe(this@StoryActivity) { token ->
//                getStoryList(token)
//            }
//            isLoading.observe(this@StoryActivity) { isLoading ->
//                showLoading(isLoading)
//            }
//            message.observe(this@StoryActivity) { msg ->
//                Toast.makeText(this@StoryActivity, msg, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.logout -> {
//                mainViewModel.logout()
//                val intent = Intent(this, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                finish()
//                startActivity(intent)
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}