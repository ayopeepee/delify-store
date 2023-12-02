package com.swmpire.delifyit.presentation.ui.main.tabs.items

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val adapter = ItemsTabListAdapter(this, itemsViewModel)
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
        itemsViewModel.getAllItems()
        itemsViewModel.selectedItemsCount.observe(viewLifecycleOwner, Observer { count ->
            with(binding.toolbar) {
                if (count > 0) {
                    menu.findItem(R.id.delete).isVisible = true
                    menu.findItem(R.id.create_order).isVisible = true
                    title = count.toString()
                    navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_close)
                } else {
                    menu.findItem(R.id.delete).isVisible = false
                    menu.findItem(R.id.create_order).isVisible = false
                    title = resources.getString(R.string.items)
                    navigationIcon = null
                }
                setNavigationOnClickListener {
                    adapter.deselectAll()
                }
                menu.findItem(R.id.delete).setOnMenuItemClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.approvement))
                        .setMessage(resources.getString(R.string.delete_confirm))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            itemsViewModel.deleteSelectedItems()
                            menu.findItem(R.id.delete).isVisible = false
                            title = resources.getString(R.string.items)
                            navigationIcon = null
                        }
                        .show()

                    true
                }
                menu.findItem(R.id.create_order).setOnMenuItemClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.approvement))
                        .setMessage(resources.getString(R.string.create_order_confirm))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            itemsViewModel.createOrder()
                        }
                        .show()
                    true
                }
            }
        })
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    itemsViewModel.itemsFlow.collect() { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    recyclerViewItems.veil()
                                    textViewNoItems.visibility = View.GONE
                                    lottieView.visibility = View.GONE
                                }
                            }

                            is NetworkResult.Success -> {
                                if (result.data != null) {
                                    with(binding) {
                                        recyclerViewItems.unVeil()
                                        swipeRefresh.isRefreshing = false
                                    }
                                }
                            }

                            is NetworkResult.Error -> {
                                with(binding) {
                                    when (result.message) {
                                        "nothing to show" -> {
                                            recyclerViewItems.unVeil()
                                            swipeRefresh.isRefreshing = false
                                            textViewNoItems.visibility = View.VISIBLE
                                            lottieView.visibility = View.VISIBLE
                                        }

                                        else -> {
                                            Toast.makeText(
                                                requireContext(),
                                                result.message,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }

                            is NetworkResult.Idle -> {}
                        }
                    }
                }
                launch {
                    itemsViewModel.itemsCallbackFlow.collect { result ->
                        adapter.submitData(result)
                    }
                }
                launch {
                    itemsViewModel.deleteSelectedItems.collect { result ->
                        when (result) {
                            is NetworkResult.Loading -> {}

                            is NetworkResult.Success -> {
                                adapter.deselectAll()
                                itemsViewModel.getAllItems()
                            }

                            is NetworkResult.Error -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is NetworkResult.Idle -> {}
                        }
                    }
                }
                launch {
                    itemsViewModel.createOrderFlow.collect { result ->
                        when(result) {
                            is NetworkResult.Loading -> {}
                            is NetworkResult.Success -> {
                                adapter.deselectAll()
                            }
                            is NetworkResult.Error -> {
                                adapter.deselectAll()
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
        itemsViewModel.deselectAllItems()
        _binding = null
    }

}