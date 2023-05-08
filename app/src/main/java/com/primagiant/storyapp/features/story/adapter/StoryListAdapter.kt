package com.primagiant.storyapp.features.story.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.primagiant.storyapp.data.response.ListStoryItem
import com.primagiant.storyapp.databinding.ItemStoryBinding

class StoryListAdapter(
    private var storyList: List<ListStoryItem>,
    private val context: Context
) : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    private lateinit var onStoryListClickCallback: OnStoryListClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = storyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            authorItem.text = storyList[position].name
            Glide.with(context)
                .load(storyList[position].photoUrl)
                .into(imageItem)
        }
        holder.itemView.setOnClickListener {
            onStoryListClickCallback.onItemClicked(
                storyList[holder.adapterPosition]
            )
        }
    }

    fun setOnStoryListClickCallback(onStoryListClickCallback: OnStoryListClickCallback) {
        this.onStoryListClickCallback = onStoryListClickCallback
    }

    interface OnStoryListClickCallback {
        fun onItemClicked(story: ListStoryItem)
    }

    class ViewHolder(
        val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

}