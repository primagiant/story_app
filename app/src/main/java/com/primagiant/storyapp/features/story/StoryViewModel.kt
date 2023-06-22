package com.primagiant.storyapp.features.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.primagiant.storyapp.data.StoryRepository
import com.primagiant.storyapp.data.local.preference.SettingPreference
import com.primagiant.storyapp.data.remote.response.DetailStoryResponse
import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.data.remote.response.NewStoryResponse
import com.primagiant.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(
    private val repository: StoryRepository,
    private val pref: SettingPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory: LiveData<DetailStoryResponse> = _detailStory

    private var token: String

    init {
        runBlocking {
            token = pref.getToken().first()
        }
    }

    val listStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStory(token).cachedIn(viewModelScope)

    fun getDetailStory(idStory: String) {
        _isLoading.value = true

        val bearerToken = "Bearer $token"

        val client = ApiConfig
            .getApiService()
            .detailStory(bearerToken, idStory)

        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailStory.value = response.body()
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    fun addStory(desc: RequestBody, photo: MultipartBody.Part, lat: Float?, lon: Float?) {
        _isLoading.value = true

        val bearerToken = "Bearer $token"

        val client = ApiConfig
            .getApiService()
            .addStory(bearerToken, photo, desc, lat, lon)

        client.enqueue(object : Callback<NewStoryResponse> {
            override fun onResponse(
                call: Call<NewStoryResponse>,
                response: Response<NewStoryResponse>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    _message.value = response.message()
                    Log.e("Add 1", response.message())
                }
            }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
                Log.e("Add 2", t.message.toString())
            }

        })
    }

}