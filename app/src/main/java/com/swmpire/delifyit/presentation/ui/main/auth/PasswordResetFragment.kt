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
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentPasswordResetBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordResetFragment : Fragment() {

    private val passwordResetViewModel: PasswordResetViewModel by viewModels()

    private var _binding: FragmentPasswordResetBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordResetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonReset.setOnClickListener {
            // TODO: add validation
            if (!binding.textInputEmail.text.isNullOrBlank()) {
                passwordResetViewModel.resetPassword(email = binding.textInputEmail.text.toString().trim())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                passwordResetViewModel.resetPasswordFlow.collect() { result ->
                    when(result) {
                        is NetworkResult.Loading -> {
                            // TODO: add progress indicator
                            with(binding){
                                buttonReset.isEnabled = false
                                textViewError.visibility = View.GONE
                            }
                        }
                        is NetworkResult.Error -> {
                            binding.buttonReset.isEnabled = true
                            Toast.makeText(requireContext(), "${result.message}", Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResult.Success -> {
                            with(binding){
                                textViewError.visibility = View.GONE
                            }
                            if (result.data == true) {
                                findNavController().navigate(PasswordResetFragmentDirections.actionPasswordResetFragmentToSignInFragment())
                            } else {
                                with(binding){
                                    textViewError.text = "Произошла ошибка"
                                    textViewError.visibility = View.VISIBLE
                                }
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