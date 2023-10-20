package com.swmpire.delifyit.presentation.ui.main.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.swmpire.delifyit.databinding.FragmentSignUpBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by viewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToTextObservers()

        with(binding) {
            buttonNext.setOnClickListener {

                val confirmPassword = textInputConfirmPassword.text.toString().trim()
                // TODO: add email and confirm password validation too
                if (PasswordValidator.validate(textInputPassword, layoutInputPassword)) {
                    signUpViewModel.signUpStore(StoreModel(
                        email = textInputEmail.text.toString().trim(),
                        password = textInputPassword.text.toString().trim()
                    ))
                }
            }
            textViewSignupToSignin.setOnClickListener{
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.signUpFlow.collect { result ->
                    when(result) {
                        is NetworkResult.Loading -> {
                            // TODO: add progress indicator
                            binding.buttonNext.isEnabled = false
                            binding.layoutInputPassword.error = null
                        }
                        is NetworkResult.Error -> {
                            when (result.message) {
                                "The email address is badly formatted" -> {
                                    //binding.layoutInputEmail.error = "Некорректный email"
                                }
                                "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                                    //binding.layoutInputPassword.error = "Минимум 6 символов"
                                }
                            }

                            binding.buttonNext.isEnabled = true
                        }
                        is NetworkResult.Success -> {
                            binding.buttonNext.isEnabled = true
                            if (result.data == true) {
                                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToTabsFragment())
                            } else {
                                Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is NetworkResult.Idle -> {}
                    }
                }
            }
        }

    }

    private fun subscribeToTextObservers() {
        PasswordTextChangeObserver(PasswordValidator).observe(binding.textInputPassword, binding.layoutInputPassword)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}