package com.swmpire.delifyit.presentation.ui.main.tabs.profile

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
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentUpdateStoreBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.utils.ItemCategory
import com.swmpire.delifyit.utils.StoreTypes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UpdateStoreFragment : Fragment() {

    private val updateStoreViewModel: UpdateStoreViewModel by viewModels()
    private var _binding: FragmentUpdateStoreBinding? = null
    private val binding get() = _binding!!
    private var newImageUrl: String? = null
    private var storeBeforeChanges: StoreModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateStoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dropDownMenuAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_list_item_type,
            StoreTypes.types
        )
        binding.autoCompleteSelectType.setAdapter(dropDownMenuAdapter)

        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                binding.circleImageViewProfile.setImageURI(uri)
                if (uri != null) {
                    updateStoreViewModel.uploadProfilePicture(uri)
                }
            }

        with(binding) {
            circleImageViewProfile.setOnClickListener {
                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            buttonCancel.setOnClickListener {
                confirmCancel()
            }
            toolbar.setNavigationOnClickListener {
                confirmCancel()
            }
            textInputName.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }
            textInputDescription.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }
            textInputAddress.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }
            autoCompleteSelectType.doOnTextChanged { _, _, _, _ -> buttonSave.isEnabled = true }

            buttonSave.setOnClickListener {
                // TODO: add validation
                updateStoreViewModel.updateStore(
                    StoreModel(
                        name = if (storeBeforeChanges?.name == textInputName.text.toString()) null
                        else textInputName.text.toString(),
                        description = if (storeBeforeChanges?.description == textInputDescription.text.toString()) null
                        else textInputDescription.text.toString(),
                        address = if (storeBeforeChanges?.address == textInputAddress.text.toString()) null
                        else textInputAddress.text.toString(),
                        type = if (storeBeforeChanges?.type == autoCompleteSelectType.text.toString()) null
                        else autoCompleteSelectType.text.toString(),
                        profilePictureUrl = if (storeBeforeChanges?.profilePictureUrl == newImageUrl) null
                        else newImageUrl.toString(),
                        isVerified = false
                    )
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    updateStoreViewModel.updateStoreFlow.collect { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.VISIBLE
                                    textViewError.visibility = View.GONE
                                    buttonSave.isEnabled = false
                                    buttonCancel.isEnabled = false
                                }
                            }

                            is NetworkResult.Success -> {
                                if (result.data == true) {
                                    findNavController().popBackStack()
                                }
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
                launch {
                    updateStoreViewModel.uploadProfilePictureFlow.collect { result ->
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
                                    buttonSave.isEnabled = true
                                }
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
                launch {
                    updateStoreViewModel.getStoreFlow.collect { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.VISIBLE
                                    textViewError.visibility = View.GONE
                                }
                            }

                            is NetworkResult.Success -> {
                                val store = result.data
                                if (store != null) {
                                    with(binding) {
                                        progressHorizontal.visibility = View.GONE
                                        textViewError.visibility = View.GONE

                                        toolbar.title = store.name
                                        textInputName.setText(store.name)
                                        textInputDescription.setText(store.description)
                                        textInputAddress.setText(store.address)
                                        autoCompleteSelectType.setText(store.type, true)

                                        buttonSave.isEnabled = false

                                        Glide.with(requireContext())
                                            .load(store.profilePictureUrl)
                                            .placeholder(R.drawable.placeholder_image)
                                            .into(circleImageViewProfile)
                                    }

                                    storeBeforeChanges = store
                                    newImageUrl = store.profilePictureUrl
                                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}