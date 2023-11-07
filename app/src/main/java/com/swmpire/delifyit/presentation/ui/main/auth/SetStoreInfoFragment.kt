package com.swmpire.delifyit.presentation.ui.main.auth

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
import com.swmpire.delifyit.databinding.FragmentSetStoreInfoBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetStoreInfoFragment : Fragment() {

    private val setStoreInfoViewModel: SetStoreInfoViewModel by viewModels()

    private var _binding: FragmentSetStoreInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetStoreInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dropDownMenuAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_list_item_type, MenuItems.items)
        binding.autoCompleteSelectType.setAdapter(dropDownMenuAdapter)

        with(binding) {

            buttonNext.setOnClickListener {
                // TODO: add validation
                if (textInputName.text.toString().isNotBlank()
                    && textInputDescription.text.toString().isNotBlank()
                    && textInputAddress.text.toString().isNotBlank()
                    && autoCompleteSelectType.text.toString().isNotBlank()) {
                    setStoreInfoViewModel.createStore(
                        textInputName.text.toString(),
                        textInputDescription.text.toString(),
                        textInputAddress.text.toString(),
                        autoCompleteSelectType.text.toString())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                setStoreInfoViewModel.createStoreFlow.collect() {result ->
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
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
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
        val items = arrayOf("Заведение", "Магазин", "Другое")
    }
}