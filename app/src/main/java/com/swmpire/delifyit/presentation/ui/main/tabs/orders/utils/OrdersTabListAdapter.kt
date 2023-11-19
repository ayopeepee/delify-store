package com.swmpire.delifyit.presentation.ui.main.tabs.orders.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.OrdersListItemBinding
import com.swmpire.delifyit.domain.model.OrderModel
import com.swmpire.delifyit.presentation.ui.main.tabs.orders.OrdersViewModel

class OrdersTabListAdapter(private val ordersViewModel: OrdersViewModel) : RecyclerView.Adapter<OrdersTabListAdapter.OrdersTabViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersTabViewHolder {
        val binding =
            OrdersListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersTabViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
    override fun onBindViewHolder(holder: OrdersTabViewHolder, position: Int) {
        val order = asyncListDiffer.currentList[position]
        val orderId = order.id
        val orderItems = order.items

        if (orderId!= null && orderItems != null) {
            val orderItemsAdapter = OrderItemsListAdapter(orderItems)
            with(holder.binding) {
                apply {
                    textViewOrderId.text = holder.itemView.context
                        .getString(R.string.order_id, orderId).split('-').first()
                    textViewOrderStatus.text = order.orderStatus

                    when(order.orderStatus) {
                        "Готов" -> buttonDone.isEnabled = false
                        "Отменен" -> {
                            buttonDone.isEnabled = false
                            buttonCancel.isEnabled = false
                        }
                    }

                    recyclerViewOrderItems.layoutManager = LinearLayoutManager(holder.itemView.context)
                    recyclerViewOrderItems.adapter = orderItemsAdapter
                }
                buttonCancel.setOnClickListener {
                    ordersViewModel.cancelOrder(orderId)
                }
                buttonDone.setOnClickListener {
                    ordersViewModel.placeOrder(orderId)
                }
            }
        }
    }

    class OrdersTabViewHolder(var binding: OrdersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    fun submitData(data: List<OrderModel>) = asyncListDiffer.submitList(data)
    object OrderCallback : DiffUtil.ItemCallback<OrderModel>() {
        override fun areItemsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean =
            oldItem == newItem
    }

    private val asyncListDiffer = AsyncListDiffer(this, OrderCallback)


}