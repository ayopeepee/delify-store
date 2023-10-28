package com.swmpire.delifyit.presentation.ui.main.tabs.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentAddItemBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.presentation.ui.main.auth.SetStoreInfoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private val addItemViewModel: AddItemViewModel by viewModels()
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dropDownMenuAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_list_item_category,
            MenuItems.items
        )
        binding.autoCompleteSelectCategory.setAdapter(dropDownMenuAdapter)

        with(binding) {
            buttonNext.setOnClickListener {
                // TODO: add validation
                if (textInputName.text.toString().isNotBlank()
                    && textInputDescription.text.toString().isNotBlank()
                    && textInputPrice.text.toString().isNotBlank()
                    && autoCompleteSelectCategory.text.toString().isNotBlank()
                ) {
                    addItemViewModel.addItem(
                        textInputName.text.toString(),
                        textInputDescription.text.toString(),
                        autoCompleteSelectCategory.text.toString(),
                        textInputPrice.text.toString().toInt()
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addItemViewModel.addItemFlow.collect() { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            // TODO: add progress indicator
                            with(binding) {
                                buttonNext.isEnabled = false
                            }
                        }

                        is NetworkResult.Success -> {
                            with(binding) {
                                buttonNext.isEnabled = true
                                if (result.data == true) {
                                    findNavController().popBackStack()
                                }
                            }
                        }

                        is NetworkResult.Error -> {
                            // TODO: add error view
                            with(binding) {
                                buttonNext.isEnabled = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object MenuItems {
        val items = arrayOf("Еда", "Товар")
    }
}