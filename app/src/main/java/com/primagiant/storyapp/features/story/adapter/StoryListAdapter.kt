package com.primagiant.storyapp.features.story.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.databinding.ItemStoryBinding
import com.primagiant.storyapp.features.story.DetailStoryActivity

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ViewHolder(
        private val binding: ItemStoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {

            binding.apply {
                authorItem.text = data.name
                descItem.text = data.description
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imageItem)
            }
            itemView.setOnClickListener {
                val goToDetailActivity = Intent(itemView.context, DetailStoryActivity::class.java)
                goToDetailActivity.putExtra(DetailStoryActivity.EXTRA_ID, data.id)
                goToDetailActivity.putExtra(DetailStoryActivity.EXTRA_STORY_ITEMS, data)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.descItem, "detail_thumbnail"),
                        Pair(binding.authorItem, "detail_name")
                    )
                itemView.context.startActivity(goToDetailActivity, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}