package com.primagiant.storyapp.utils

import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.data.remote.response.LoginResponse
import com.primagiant.storyapp.data.remote.response.LoginResult
import com.primagiant.storyapp.data.remote.response.RegisterResponse

object DummyData {

    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..50) {
            val story = ListStoryItem(
                "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "created + $i",
                "name + $i",
                "description + $i",
                null,
                null,
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateEmptyDummyStoryEntity() : List<ListStoryItem> {
        return emptyList()
    }
    fun generateDummyStoryWithLocationEntity(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..50) {
            val story = ListStoryItem(
                "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "created + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                i.toDouble(),
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyLoginEntity() = LoginResponse(
        LoginResult(
            "name",
            "id",
            "token"
        ),
        false,
        "token"
    )

    fun generateDummyRegisterEntity() = RegisterResponse(
        false,
        "success"
    )

}