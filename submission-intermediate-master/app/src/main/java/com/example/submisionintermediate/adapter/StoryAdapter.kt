package com.example.submisionintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.DiffingChangePayload
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submisionintermediate.R
import com.example.submisionintermediate.data.response.ListStoryItem
import com.example.submisionintermediate.databinding.ItemStoryBinding

class StoryAdapter (private val onItemeClick:(ListStoryItem)->Unit): ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)
        holder.itemView.setOnClickListener{
            if (storyItem != null) {
                onItemeClick.invoke(storyItem)
            }
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem?) {
            if (storyItem != null) {
                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .into(binding.imageStory)
                binding.judulStory.text=storyItem.name
                binding.desStory.text=storyItem.description
            }
    }
}

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
