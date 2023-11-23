package com.swmpire.delifyit.presentation.ui.main.tabs.items

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentAddItemBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.presentation.ui.main.tabs.utils.InputTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.tabs.utils.TextValidator
import com.swmpire.delifyit.utils.ItemCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private val addItemViewModel: AddItemViewModel by viewModels()
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUrl: String? = null
    @Inject lateinit var inputTextChangeObserver: InputTextChangeObserver
    @Inject lateinit var textValidator: TextValidator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToTextObservers()

        val dropDownMenuAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_list_item_category,
            ItemCategory.categories
        )
        binding.autoCompleteSelectCategory.setAdapter(dropDownMenuAdapter)

        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                binding.imageViewItem.setImageURI(uri)
                if (uri != null) {
                    addItemViewModel.addItemImage(uri)
                    Log.d("TAG", "onViewCreated: $uri")
                }
            }

        with(binding) {
            buttonNext.setOnClickListener {

                if (selectedImageUrl.isNullOrBlank()) {
                    Snackbar.make(
                        binding.scrollView,
                        resources.getString(R.string.select_image),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                if (autoCompleteSelectCategory.text.toString().isBlank()) {
                    Snackbar.make(
                        binding.scrollView,
                        resources.getString(R.string.select_category),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                if (textValidator.validate(textInputName, layoutInputName)
                    && textValidator.validate(textInputDescription, layoutInputDescription)
                    && textValidator.validate(textInputPrice, layoutInputPrice)
                    && autoCompleteSelectCategory.text.toString().isNotBlank()
                    && !selectedImageUrl.isNullOrBlank()
                ) {
                    addItemViewModel.addItem(
                        textInputName.text.toString(),
                        textInputDescription.text.toString(),
                        autoCompleteSelectCategory.text.toString(),
                        textInputPrice.text.toString().toInt(),
                        selectedImageUrl.toString()
                    )
                }
            }
            cardViewImageWrapper.setOnClickListener {
                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            toolbar.setNavigationOnClickListener {
                confirmCancel()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    addItemViewModel.addItemFlow.collect() { result ->
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
                                        findNavController().popBackStack()
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
                    addItemViewModel.addItemImageFlow.collect() { result ->
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

    private fun confirmCancel() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.approvement))
            .setMessage(resources.getString(R.string.changes_discard))
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                findNavController().popBackStack()
            }
            .show()
    }

    private fun subscribeToTextObservers() {
        with(binding) {
            inputTextChangeObserver.run {
                observe(textInputName, layoutInputName)
                observe(textInputDescription, layoutInputDescription)
                observe(textInputPrice, layoutInputPrice)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}