package com.primagiant.storyapp.features.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.datastore.AuthPreferences
import com.primagiant.storyapp.databinding.FragmentRegisterBinding
import com.primagiant.storyapp.features.auth.AuthViewModel
import com.primagiant.storyapp.features.auth.AuthViewModelFactory
import com.primagiant.storyapp.features.auth.login.LoginFragment
import com.primagiant.storyapp.features.story.StoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = AuthPreferences.getInstance(requireContext().dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]

        binding.apply {
            // Validate Password
            inputPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(c: CharSequence?, start: Int, end: Int, count: Int) {
                    if (c.toString().length < 8) {
                        errorPassword.visibility = View.VISIBLE
                    } else {
                        errorPassword.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            // Validate Email
            inputEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(c: CharSequence?, start: Int, end: Int, count: Int) {
                    if (!isEmailValid(c.toString())) {
                        errorEmail.visibility = View.VISIBLE
                    } else {
                        errorEmail.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            // On Button Click
            buttonRegister.setOnClickListener {
                val valid = validateInput(
                    inputName.text.toString(),
                    inputEmail.text.toString(),
                    inputPassword.text.toString()
                )
                if (valid) {
                    authViewModel.apply {
                        register(
                            inputName.text.toString(),
                            inputEmail.text.toString(),
                            inputPassword.text.toString()
                        )
                        isLoading.observe(requireActivity()) { isLoading ->
                            showLoading(isLoading)
                        }
                        message.observe(requireActivity()) { msg ->
                            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                        }
                        getToken().observe(requireActivity()) { token ->
                            isLogin(token)
                        }
                    }
                }
            }

            linkLogin.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_left_to_right,
                        R.anim.slide_left_to_right
                    )
                    .replace(
                        R.id.main_container,
                        LoginFragment(),
                        LoginFragment::class.java.simpleName
                    ).commit()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$").matches(email)
    }

    private fun isLogin(token: String) {
        if (token != "") {
            val intent = Intent(requireActivity(), StoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}