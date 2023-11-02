package com.swmpire.delifyit.presentation.ui.main.tabs.items

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentItemsBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.presentation.ui.main.tabs.items.utils.ItemsTabListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private val itemsViewModel: ItemsViewModel by viewModels()
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
        val adapter = ItemsTabListAdapter()
        with(binding) {
            buttonAddItem.setOnClickListener {
                findNavController().navigate(ItemsFragmentDirections.actionItemsFragmentToAddItemFragment())
            }
            recyclerViewItems.setAdapter(adapter)
            recyclerViewItems.setLayoutManager(GridLayoutManager(requireContext(), 2))
            recyclerViewItems.addVeiledItems(6)

            swipeRefresh.setOnRefreshListener {
                itemsViewModel.getAllItems()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    itemsViewModel.itemsFlow.collect() { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                binding.recyclerViewItems.veil()
                            }
                            is NetworkResult.Success -> {
                                if (result.data != null) {
                                    binding.recyclerViewItems.unVeil()
                                    binding.swipeRefresh.isRefreshing = false
                                }
                            }

                            is NetworkResult.Error -> { TODO("observe state") }
                            is NetworkResult.Idle -> {}
                        }
                    }
                }
                launch {
                    itemsViewModel.itemsCallbackFlow.collect { result ->
                        adapter.submitData(result)
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