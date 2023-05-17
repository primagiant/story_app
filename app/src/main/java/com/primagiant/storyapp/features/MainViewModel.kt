package com.primagiant.storyapp.features

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.primagiant.storyapp.data.api.ApiConfig
import com.primagiant.storyapp.data.local.repository.AuthRepository
import com.primagiant.storyapp.data.response.AllStoryResponse
import com.primagiant.storyapp.data.response.DetailStoryResponse
import com.primagiant.storyapp.data.response.ListStoryItem
import com.primagiant.storyapp.data.response.LoginResponse
import com.primagiant.storyapp.data.response.NewStoryResponse
import com.primagiant.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storyList

    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory: LiveData<DetailStoryResponse> = _detailStory

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    init {
        getToken()
    }

    fun getStoryList(token: String) {
        _isLoading.value = true

        val client = ApiConfig
            .getApiService("Bearer $token")
            .getAllStory()

        client.enqueue(object : Callback<AllStoryResponse> {
            override fun onResponse(
                call: Call<AllStoryResponse>,
                response: Response<AllStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyList.value = response.body()?.listStory
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    fun getDetailStory(idStory: String, token: String) {
        _isLoading.value = true

        val client = ApiConfig
            .getApiService("Bearer $token")
            .detailStory(idStory)

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

    fun addStory(desc: RequestBody, photo: MultipartBody.Part, token: String) {
        _isLoading.value = true

        val client = ApiConfig
            .getApiService("Bearer $token")
            .addStory(desc, photo)

        client.enqueue(object : Callback<NewStoryResponse> {
            override fun onResponse(
                call: Call<NewStoryResponse>,
                response: Response<NewStoryResponse>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val loginResult = response.body()?.loginResult
                    loginResult?.token?.let { saveToken(it) }

                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(name, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = response.message()
                    login(email, password)
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    private fun saveToken(token: String) {
        viewModelScope.launch {
            authRepository.saveToken(token)
        }
    }

    private fun getToken() {
        viewModelScope.launch {
            authRepository.getToken().collect {
                _token.postValue(it)
            }
        }
    }

    fun logout() {
        saveToken("")
    }

}