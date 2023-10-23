package com.swmpire.delifyit.presentation.ui.main.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentSetStoreInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetStoreInfoFragment : Fragment() {

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
        val dropDownMenuAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_list_item, MenuItems.items)
        binding.autoCompleteSelectType.setAdapter(dropDownMenuAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object MenuItems {
        val items = arrayOf("Заведение", "Магазин", "Другое")
    }
}