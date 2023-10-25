package com.swmpire.delifyit.presentation.ui.main.auth

import android.os.Bundle
import android.util.Log
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
import com.swmpire.delifyit.presentation.ui.main.auth.utils.EmailTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.auth.utils.EmailValidator
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordConfirmValidator
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordConfirmTextChangeObserver
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

                if (PasswordValidator.validate(textInputPassword, layoutInputPassword)
                    && EmailValidator.validate(textInputEmail, layoutInputEmail)
                    && PasswordConfirmValidator.validate(
                        textInputPassword,
                        textInputConfirmPassword,
                        layoutConfirmPassword
                    )
                ) {
                    signUpViewModel.signUpStore(
                        email = textInputEmail.text.toString().trim(),
                        password = textInputPassword.text.toString().trim()
                    )
                }
            }
            textViewSignupToSignin.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.signUpFlow.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            with(binding) {
                                progressHorizontal.visibility = View.VISIBLE
                                buttonNext.isEnabled = false
                                layoutInputPassword.error = null
                                textViewError.visibility = View.GONE
                            }
                        }

                        is NetworkResult.Error -> {
                            binding.progressHorizontal.visibility = View.GONE
                            binding.buttonNext.isEnabled = true
                            when (result.message) {
                                "The email address is already in use by another account." -> {
                                    with(binding.textViewError) {
                                        text = "Аккаунт с таким email уже существует"
                                        visibility = View.VISIBLE
                                    }
                                }

                                else -> {
                                    with(binding.textViewError) {
                                        text = "Произошла ошибка"
                                        visibility = View.VISIBLE
                                    }
                                }
                            }
                        }

                        is NetworkResult.Success -> {
                            with(binding) {
                                progressHorizontal.visibility = View.GONE
                                textViewError.visibility = View.GONE
                            }

                            if (result.data == true) {
                                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSetStoreInfoFragment())
                            } else {
                                with(binding) {
                                    textViewError.text = "Произошла ошибка"
                                    textViewError.visibility = View.VISIBLE
                                    progressHorizontal.visibility = View.GONE
                                }
                            }
                        }

                        is NetworkResult.Idle -> {}
                    }
                }
            }
        }

    }

    private fun subscribeToTextObservers() {
        PasswordTextChangeObserver(PasswordValidator).observe(
            binding.textInputPassword,
            binding.layoutInputPassword
        )
        EmailTextChangeObserver(EmailValidator).observe(
            binding.textInputEmail,
            binding.layoutInputEmail
        )
        PasswordConfirmTextChangeObserver(PasswordConfirmValidator).observe(
            binding.textInputPassword,
            binding.textInputConfirmPassword,
            binding.layoutConfirmPassword
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}