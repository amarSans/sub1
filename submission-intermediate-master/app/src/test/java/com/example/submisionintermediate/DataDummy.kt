package com.example.submisionintermediate

import com.example.submisionintermediate.data.database.storyItem

object DataDummy {
    fun generateDummyStoryItems(): List<storyItem> {
        val items: MutableList<storyItem> = arrayListOf()
        for (i in 0 .. 100) {
            val story = storyItem(
                "story-hQugomFBj9J1vPUT",
                "amar ",
                "semangat ",
                "https://story-api.dicoding.dev/images/stories/photos-1701985520573_4AbNn8Pb.20231208_0544344926430627656631211jpg",
                null,
                null,
                null
            )
            items.add(story)
        }
        return items
    }
}