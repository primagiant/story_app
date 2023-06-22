package com.primagiant.storyapp.features.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.primagiant.storyapp.data.remote.response.LoginResult
import com.primagiant.storyapp.data.remote.response.RegisterResponse
import com.primagiant.storyapp.utils.DummyData
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authViewModel: AuthViewModel
    private val loginData = DummyData.generateDummyLoginEntity()
    private val registerData = DummyData.generateDummyRegisterEntity()

    @Test
    fun register() {
        val observer = Observer<RegisterResponse> {}

        try {
            val expectedUser = MutableLiveData<RegisterResponse>()
            expectedUser.value = registerData
            Mockito.`when`(authViewModel.registerResult).thenReturn(expectedUser)

            val actualUser = MutableLiveData<LoginResult>()
            authViewModel.registerResult.observeForever(observer)
            authViewModel.register(NAME, EMAIL, PASSWORD)

            Mockito.verify(authViewModel).register(NAME, EMAIL, PASSWORD)
            assertNotNull(actualUser)
        } finally {
            authViewModel.registerResult.removeObserver(observer)
        }

    }

    @Test
    fun login() {
        val observer = Observer<LoginResult> {}

        try {
            val expectedUser = MutableLiveData<LoginResult>()
            expectedUser.value = loginData.loginResult
            Mockito.`when`(authViewModel.loginResult).thenReturn(expectedUser)

            val actualUser = MutableLiveData<LoginResult>()
            authViewModel.loginResult.observeForever(observer)
            authViewModel.login(EMAIL, PASSWORD)

            Mockito.verify(authViewModel).login(EMAIL, PASSWORD)
            assertNotNull(actualUser)
        } finally {
            authViewModel.loginResult.removeObserver(observer)
        }
    }

    companion object {
        private const val NAME = "dummyName"
        private const val EMAIL = "dummyEmail"
        private const val PASSWORD = "password"
    }
}