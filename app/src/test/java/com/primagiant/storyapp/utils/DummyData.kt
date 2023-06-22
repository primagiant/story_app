package com.primagiant.storyapp.utils

import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.data.remote.response.LoginResponse
import com.primagiant.storyapp.data.remote.response.LoginResult
import com.primagiant.storyapp.data.remote.response.RegisterResponse

object DummyData {

    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "2022-01-08T06:34:18.598Z",
                "Dimas",
                "Lorem Ipsum",
                -16.002,
                -10.212,
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