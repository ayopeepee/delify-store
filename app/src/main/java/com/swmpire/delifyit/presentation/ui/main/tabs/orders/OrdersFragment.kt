package com.swmpire.delifyit.presentation.ui.main.tabs.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentOrdersBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.presentation.ui.main.tabs.orders.utils.OrdersTabListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private val ordersViewModel: OrdersViewModel by viewModels()
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OrdersTabListAdapter(ordersViewModel)

        with(binding) {
            recyclerViewOrders.setAdapter(adapter)
            recyclerViewOrders.setLayoutManager(LinearLayoutManager(requireContext()))
            recyclerViewOrders.addVeiledItems(4)
            recyclerViewOrders.isNestedScrollingEnabled = false

            swipeRefresh.setOnRefreshListener {
                ordersViewModel.getOrders()
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    ordersViewModel.getOrdersFlow.collect { result ->
                        when(result) {
                            is NetworkResult.Loading -> {
                                binding.recyclerViewOrders.veil()
                            }
                            is NetworkResult.Success -> {
                                if (result.data != null) {
                                    with(binding) {
                                        recyclerViewOrders.unVeil()
                                        swipeRefresh.isRefreshing = false
                                    }
                                }
                            }
                            is NetworkResult.Error -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Idle -> {}
                        }
                    }
                }
                launch {
                    ordersViewModel.getOrdersCallbackFlow.collect { result ->
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