package com.primagiant.storyapp.features.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.primagiant.storyapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isEmailValid(email: String): Boolean {
        return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$").matches(email)
    }

}