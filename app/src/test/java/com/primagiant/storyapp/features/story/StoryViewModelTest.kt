package com.primagiant.storyapp.features.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.primagiant.storyapp.data.StoryRepository
import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.features.story.adapter.StoryListAdapter
import com.primagiant.storyapp.utils.DummyData
import com.primagiant.storyapp.utils.MainDispatcherRule
import com.primagiant.storyapp.utils.PagingTestDataSource
import com.primagiant.storyapp.utils.getOrAwait
import com.primagiant.storyapp.utils.noopListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: StoryRepository

    @Test
    fun `check getStory not null and the the data size as expected`() = runTest {
        val dummyStory = DummyData.generateDummyStoryEntity()
        val data: PagingData<ListStoryItem> = PagingTestDataSource.snapshot(dummyStory)

        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data

        Mockito.`when`(repository.getStory(TOKEN)).thenReturn(story)

        val viewModel = StoryViewModel(repository)
        val actualStory: PagingData<ListStoryItem> = viewModel.listStory(TOKEN).getOrAwait()

        val diff = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        diff.submitData(actualStory)

        advanceUntilIdle()

        // Memastikan data tidak null
        Assert.assertNotNull(diff.snapshot())

        // Memastikan data sesuai expectansi
        Assert.assertEquals(dummyStory.size, diff.snapshot().size)

        // Memastikan data pertama yang dikembalikan sesuai
        Assert.assertEquals(dummyStory[0], diff.snapshot()[0])
    }

    @Test
    fun `check getStory if data is empty`() = runTest {
        val dummyStory = DummyData.generateEmptyDummyStoryEntity()
        val data: PagingData<ListStoryItem> = PagingTestDataSource.snapshot(dummyStory)

        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data

        Mockito.`when`(repository.getStory(TOKEN)).thenReturn(story)

        val viewModel = StoryViewModel(repository)
        val actualStory: PagingData<ListStoryItem> = viewModel.listStory(TOKEN).getOrAwait()

        val diff = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        diff.submitData(actualStory)

        advanceUntilIdle()

        // Memastikan data tidak null
        Assert.assertNotNull(diff.snapshot())

        // Memastikan jumlah data yang dikembalikan nol
        Assert.assertEquals(0, diff.snapshot().size)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }
}

