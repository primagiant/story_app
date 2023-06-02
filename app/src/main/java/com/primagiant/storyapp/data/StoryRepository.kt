package com.primagiant.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.primagiant.storyapp.data.local.room.MainDatabase
import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.data.remote.retrofit.ApiService

class StoryRepository(
    private val database: MainDatabase,
    private val apiService: ApiService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }
}