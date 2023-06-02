package com.primagiant.storyapp.features.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.primagiant.storyapp.data.remote.response.LoginResponse
import com.primagiant.storyapp.data.remote.response.LoginResult
import com.primagiant.storyapp.data.remote.response.RegisterResponse
import com.primagiant.storyapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

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
                    _loginResult.value = response.body()?.loginResult
                } else {
                    Log.d("login", response.message())
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

}