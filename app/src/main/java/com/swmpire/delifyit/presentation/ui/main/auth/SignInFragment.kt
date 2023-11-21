package com.swmpire.delifyit.presentation.ui.main.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentSignInBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.presentation.ui.main.auth.utils.EmailTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.auth.utils.EmailValidator
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordConfirmTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordConfirmValidator
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.auth.utils.PasswordValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val signInViewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToTextObservers()

        with(binding) {
            buttonNext.setOnClickListener {

                if (PasswordValidator.validate(textInputPassword, layoutInputPassword)
                    && EmailValidator.validate(textInputEmail, layoutInputEmail)
                ) {
                    signInViewModel.signInStore(
                        email = textInputEmail.text.toString().trim(),
                        password = textInputPassword.text.toString().trim()
                    )
                }
            }
            textViewResetPassword.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToPasswordResetFragment())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.signInFlow.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            with(binding) {
                                buttonNext.isEnabled = false
                                progressHorizontal.visibility = View.VISIBLE
                                textViewError.visibility = View.GONE
                            }
                        }

                        is NetworkResult.Error -> {
                            with(binding) {
                                buttonNext.isEnabled = true
                                progressHorizontal.visibility = View.GONE
                                textViewError.visibility = View.VISIBLE

                                when (result.message) {
                                    "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> {
                                        textViewError.text =
                                            resources.getString(R.string.invalid_credentials)
                                    }

                                    else -> {
                                        textViewError.text =
                                            resources.getString(R.string.error_occurred)
                                    }
                                }
                            }
                        }

                        is NetworkResult.Success -> {
                            binding.buttonNext.isEnabled = true
                            if (result.data == true) {
                                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToTabsFragment())
                            } else {
                                with(binding) {
                                    progressHorizontal.visibility = View.GONE
                                    textViewError.visibility = View.VISIBLE
                                    textViewError.text =
                                        resources.getString(R.string.error_occurred)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}