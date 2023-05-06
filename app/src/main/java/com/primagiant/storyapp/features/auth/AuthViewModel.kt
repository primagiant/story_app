package com.primagiant.storyapp.features.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.primagiant.storyapp.data.api.ApiConfig
import com.primagiant.storyapp.data.local.datastore.AuthPreferences
import com.primagiant.storyapp.data.response.LoginResponse
import com.primagiant.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(
    private val pref: AuthPreferences
) : ViewModel() {

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
                    var loginResult = response.body()?.loginResult
                    loginResult?.token?.let { saveToken(it) }

                } else {
                    _message.value = response.message()
                    Log.d("Test", response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
                Log.d("Test", t.message.toString())
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
                    val registerResult = response.body()
                    if (registerResult?.message == "User Created") {
                        login(email, password)
                    }
                } else {
                    _message.value = response.message()
                    Log.d("Test", response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
                Log.d("Test", t.message.toString())
            }

        })
    }

    fun logout() {
        saveToken("")
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    private fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

}