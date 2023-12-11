package com.example.submisionintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.databinding.ItemStoryBinding

class StoryAdapter(private val onItemeClick: (storyItem) -> Unit) :
    PagingDataAdapter<storyItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyItem1 = getItem(position)
        storyItem1?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            storyItem1?.let { onItemeClick.invoke(it) }
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: storyItem) {
            Glide.with(itemView.context)
                .load(storyItem.photoUrl)
                .into(binding.imageStory)
            binding.judulStory.text = storyItem.name
            binding.desStory.text = storyItem.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<storyItem>() {
            override fun areItemsTheSame(oldItem: storyItem, newItem: storyItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: storyItem,
                newItem: storyItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
