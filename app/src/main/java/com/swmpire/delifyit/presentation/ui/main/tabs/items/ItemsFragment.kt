package com.swmpire.delifyit.presentation.ui.main.tabs.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentItemsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private var _binding: FragmentItemsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddItem.setOnClickListener {
            findNavController().navigate(ItemsFragmentDirections.actionItemsFragmentToAddItemFragment())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}