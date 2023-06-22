package com.primagiant.storyapp.features.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.utils.DummyData
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mapsViewModel: MapsViewModel
    private val storyWithLocation = DummyData.generateDummyStoryWithLocationEntity()


    @Test
    fun `check getListStory is not null`() {
        val expectedStory = MutableLiveData<List<ListStoryItem>>()
        expectedStory.value = storyWithLocation

        val actualStory = mapsViewModel.getAllStory(TOKEN)

        Mockito.verify(mapsViewModel).getAllStory(TOKEN)
        assertNotNull(actualStory)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }
}