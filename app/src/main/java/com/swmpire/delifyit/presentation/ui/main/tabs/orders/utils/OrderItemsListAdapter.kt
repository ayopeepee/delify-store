package com.swmpire.delifyit.presentation.ui.main.tabs.orders.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.OrderItemsListItemBinding

class OrderItemsListAdapter(private val items: Map<String, Int>)
    : RecyclerView.Adapter<OrderItemsListAdapter.OrderItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : OrderItemsViewHolder {
        val binding = OrderItemsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemsViewHolder, position: Int) {
        val item = items.toList()[position]
        with(holder.binding) {
            apply {
                textViewItemName.text = item.first
                textViewItemQty.text = holder.itemView.context.getString(R.string.quantity, item.second)
            }
        }
    }

    override fun getItemCount(): Int = items.size
    class OrderItemsViewHolder(var binding: OrderItemsListItemBinding) : RecyclerView.ViewHolder(binding.root) {}

}