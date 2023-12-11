package com.example.submisionintermediate.view.detailActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.data.response.ListStoryItem
import com.example.submisionintermediate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem: storyItem? = intent.getParcelableExtra("thisitem")
        storyItem?.let { story(it) }

    }
    private fun story(storyItem: storyItem){
        binding.apply {
            judulStorydeta.text=storyItem.name
            desStorydeta.text=storyItem.description
        }
        Glide.with(this)
            .load(storyItem.photoUrl)
            .into(binding.imageStorydeta)
    }
}

