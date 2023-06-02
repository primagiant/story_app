package com.primagiant.storyapp.data.local.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingPreferenceViewModel(
    private val pref: SettingPreference
) : ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun deleteToken(){
        viewModelScope.launch {
            pref.saveToken("")
        }
    }
}