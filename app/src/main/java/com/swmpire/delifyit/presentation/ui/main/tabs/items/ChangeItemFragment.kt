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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentChangeItemBinding
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.presentation.ui.main.tabs.items.utils.InputTextChangeObserver
import com.swmpire.delifyit.presentation.ui.main.tabs.items.utils.TextValidator
import com.swmpire.delifyit.utils.ItemCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChangeItemFragment : Fragment() {

    private val changeItemViewModel: ChangeItemViewModel by viewModels()
    private var _binding: FragmentChangeItemBinding? = null
    private val binding get() = _binding!!
    private val argsItem: ChangeItemFragmentArgs by navArgs()
    private var newImageUrl: String? = null
    @Inject
    lateinit var inputTextChangeObserver: InputTextChangeObserver
    @Inject
    lateinit var textValidator: TextValidator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    changeItemViewModel.addItemImage(uri)
                }
            }

        with(binding) {
            textInputName.setText(argsItem.item.name)
            textInputDescription.setText(argsItem.item.description)
            textInputPrice.setText(argsItem.item.price.toString())
            autoCompleteSelectCategory.setText(argsItem.item.category, true)
            toolbar.title = argsItem.item.name

            subscribeToTextObservers()

            cardViewImageWrapper.setOnClickListener {
                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            textInputName.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }
            textInputDescription.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }
            textInputPrice.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }
            autoCompleteSelectCategory.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }

            buttonSave.setOnClickListener {
                if (textValidator.validate(textInputName, layoutInputName)
                    && textValidator.validate(textInputDescription, layoutInputDescription)
                    && textValidator.validate(textInputPrice, layoutInputPrice)
                ) {
                    changeItemViewModel.updateItem(
                        ItemModel(
                            id = argsItem.item.id,
                            name = if (argsItem.item.name == textInputName.text.toString()) null
                            else textInputName.text.toString(),
                            description = if (argsItem.item.description == textInputDescription.text.toString()) null
                            else textInputDescription.text.toString(),
                            price = if (argsItem.item.price == textInputPrice.text.toString().toInt()) null
                            else textInputPrice.text.toString().toInt(),
                            category = if (argsItem.item.category == autoCompleteSelectCategory.text.toString()) null
                            else autoCompleteSelectCategory.text.toString(),
                            imageUrl = if (argsItem.item.imageUrl == newImageUrl) null
                            else newImageUrl.toString()
                        )
                    )
                }
            }
            buttonCancel.setOnClickListener {
                confirmCancel()
            }
            toolbar.setNavigationOnClickListener {
                confirmCancel()
            }
        }
        newImageUrl = argsItem.item.imageUrl
        Glide.with(this)
            .load(argsItem.item.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(binding.imageViewItem)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    changeItemViewModel.updateItemFlow.collect() { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    buttonSave.isEnabled = false
                                    buttonCancel.isEnabled = false
                                    progressHorizontal.visibility = View.VISIBLE
                                    textViewError.visibility = View.GONE
                                }
                            }

                            is NetworkResult.Success -> {
                                with(binding) {
                                    if (result.data == true) {
                                        findNavController().popBackStack()
                                    }
                                }
                            }

                            is NetworkResult.Error -> {
                                with(binding) {
                                    buttonSave.isEnabled = true
                                    buttonCancel.isEnabled = true
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
                    changeItemViewModel.addItemImageFlow.collect() { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.VISIBLE
                                    textViewError.visibility = View.GONE
                                }
                            }

                            is NetworkResult.Success -> {
                                newImageUrl = result.data
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
        if (binding.buttonSave.isEnabled) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.approvement))
                .setMessage(resources.getString(R.string.changes_discard))
                .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    findNavController().popBackStack()
                }
                .show()
        } else {
            findNavController().popBackStack()
        }
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