package com.swmpire.delifyit.presentation.ui.main.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentSetStoreInfoBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.utils.StoreTypes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetStoreInfoFragment : Fragment() {

    private val setStoreInfoViewModel: SetStoreInfoViewModel by viewModels()

    private var _binding: FragmentSetStoreInfoBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetStoreInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dropDownMenuAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_list_item_type,
            StoreTypes.types)

        binding.autoCompleteSelectType.setAdapter(dropDownMenuAdapter)

        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                binding.circleImageViewProfile.setImageURI(uri)
                if (uri != null) {
                    setStoreInfoViewModel.uploadProfilePicture(uri)
                }
            }

        with(binding) {

            buttonNext.setOnClickListener {
                // TODO: add validation
                if (textInputName.text.toString().isNotBlank()
                    && textInputDescription.text.toString().isNotBlank()
                    && textInputAddress.text.toString().isNotBlank()
                    && autoCompleteSelectType.text.toString().isNotBlank()
                    && !selectedImageUrl.isNullOrBlank()
                ) {
                    setStoreInfoViewModel.createStore(
                        textInputName.text.toString(),
                        textInputDescription.text.toString(),
                        textInputAddress.text.toString(),
                        autoCompleteSelectType.text.toString(),
                        selectedImageUrl.toString()
                    )
                }
            }
            circleImageViewProfile.setOnClickListener {
                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    setStoreInfoViewModel.createStoreFlow.collect() { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    buttonNext.isEnabled = false
                                    progressHorizontal.visibility = View.VISIBLE
                                    textViewError.visibility = View.GONE
                                }
                            }

                            is NetworkResult.Success -> {
                                with(binding) {
                                    buttonNext.isEnabled = true
                                    if (result.data == true) {
                                        findNavController().navigate(SetStoreInfoFragmentDirections.actionSetStoreInfoFragmentToTabsFragment())
                                    }
                                }
                            }

                            is NetworkResult.Error -> {
                                with(binding) {
                                    buttonNext.isEnabled = true
                                    progressHorizontal.visibility = View.GONE
                                    textViewError.visibility = View.VISIBLE
                                }
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is NetworkResult.Idle -> {}
                        }
                    }
                }
                launch {
                    setStoreInfoViewModel.uploadProfilePictureFlow.collect { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.VISIBLE
                                    textViewError.visibility = View.GONE
                                }
                            }

                            is NetworkResult.Success -> {
                                selectedImageUrl = result.data
                                with(binding) {
                                    progressHorizontal.visibility = View.GONE
                                    textViewError.visibility = View.GONE
                                }
                                Log.d("TAG", "Collected: ${result.data}")
                            }

                            is NetworkResult.Error -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.GONE
                                    textViewError.visibility = View.VISIBLE
                                }
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is NetworkResult.Idle -> {}
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object MenuItems {
        val items = arrayOf("Заведение", "Магазин", "Другое")
    }
}