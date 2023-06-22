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
import com.primagiant.storyapp.databinding.FragmentRegisterBinding
import com.primagiant.storyapp.utils.SettingViewModelFactory
import com.primagiant.storyapp.features.story.StoryActivity

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    private val settingPreferenceViewModel: SettingPreferenceViewModel by activityViewModels {
        SettingViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingPreferenceViewModel.getToken().observe(requireActivity()) { token ->
            isLogin(token)
        }

        binding.apply {
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
                        loginResult.observe(requireActivity()) {
                            settingPreferenceViewModel.saveToken(it.token)
                        }
                    }
                }
            }

            linkLogin.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.main_container,
                        LoginFragment(),
                        LoginFragment::class.java.simpleName
                    ).commit()
            }
        }

        animate()
    }

    private fun animate() {

        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(500)
        val inputName = ObjectAnimator.ofFloat(binding.inputName, View.ALPHA, 1f).setDuration(500)
        val inputEmail = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val inputPassword =
            ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val buttonRegister =
            ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(500)
        val tv = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)
        val linkLogin = ObjectAnimator.ofFloat(binding.linkLogin, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(tv, linkLogin)
        }

        AnimatorSet().apply {
            playSequentially(title, inputName, inputEmail, inputPassword, buttonRegister, together)
            start()
        }
    }

    private fun isLogin(token: String) {
        if (token != "") {
            val intent = Intent(requireActivity(), StoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            if (binding.inputEmail.valid && binding.inputPassword.valid) {
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