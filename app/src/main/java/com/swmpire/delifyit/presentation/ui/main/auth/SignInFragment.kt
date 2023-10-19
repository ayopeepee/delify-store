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
import com.swmpire.delifyit.databinding.FragmentSignInBinding
import com.swmpire.delifyit.domain.model.NetworkResult
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

        with(binding) {
            buttonNext.setOnClickListener {
                val email = textInputEmail.text.toString().trim()
                val password = textInputPassword.text.toString().trim()

                if (email.isNotBlank() && password.isNotBlank()) {
                    signInViewModel.signInStore(
                        email = email,
                        password = password
                    )
                } else {
                    // TODO: need to replace toast with "error view"
                    Toast.makeText(requireContext(), "Введите Email и пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.signInFlow.collect { result ->
                    when(result) {
                        is NetworkResult.Loading -> {
                            // TODO: add progress indicator
                            binding.buttonNext.isEnabled = false
                        }
                        is NetworkResult.Error -> {
                            // TODO: need to replace toast with "error view"
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                            binding.buttonNext.isEnabled = true
                        }
                        is NetworkResult.Success -> {
                            binding.buttonNext.isEnabled = true
                            if (result.data == true) {
                                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToTabsFragment())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}