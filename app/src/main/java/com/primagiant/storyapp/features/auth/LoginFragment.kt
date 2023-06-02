package com.primagiant.storyapp.features.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.preference.SettingPreferenceViewModel
import com.primagiant.storyapp.databinding.FragmentLoginBinding
import com.primagiant.storyapp.features.SettingViewModelFactory
import com.primagiant.storyapp.features.story.StoryActivity

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    private val settingPreferenceViewModel: SettingPreferenceViewModel by activityViewModels {
        SettingViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingPreferenceViewModel.getToken().observe(requireActivity()) { token ->
            isLogin(token)
        }

        binding.apply {
            // On Button Click
            buttonLogin.setOnClickListener {
                val valid = validateInput(
                    inputEmail.text.toString(),
                    inputPassword.text.toString()
                )
                if (valid) {
                    authViewModel.apply {
                        login(
                            inputEmail.text.toString(),
                            inputPassword.text.toString()
                        )
                        isLoading.observe(requireActivity()) { isLoading ->
                            showLoading(isLoading)
                        }
                        message.observe(requireActivity()) { msg ->
                            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                        }
                        loginResult.observe(requireActivity()) {
                            settingPreferenceViewModel.saveToken(it.token)
                        }
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.error_require),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            linkRegister.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.main_container,
                        RegisterFragment(),
                        RegisterFragment::class.java.simpleName
                    ).commit()
            }
        }

        animate()
    }

    private fun animate() {
        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(500)
        val inputEmail = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val inputPassword =
            ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val buttonLogin =
            ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(500)
        val tv = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val linkRegister =
            ObjectAnimator.ofFloat(binding.linkRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(linkRegister, tv)
        }

        AnimatorSet().apply {
            playSequentially(title, inputEmail, inputPassword, buttonLogin, together)
            start()
        }
    }

    private fun isLogin(token: String) {
        if (token != "") {
            val intent = Intent(requireActivity(), StoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (binding.inputPassword.valid && binding.inputEmail.valid) {
                return true
            }
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