package com.swmpire.delifyit.presentation.ui.main.tabs.items.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.protobuf.Internal.ListAdapter
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.ItemsListItemBinding
import com.swmpire.delifyit.domain.model.ItemModel

class ItemsTabListAdapter : RecyclerView.Adapter<ItemsTabListAdapter.ItemsTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsTabViewHolder {
        val binding =
            ItemsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsTabViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemsTabViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(imageViewItem)

            textViewName.text = item.name
            textViewDescription.text = item.description
            textViewPrice.text = holder.itemView.context.getString(R.string.price_tenge, item.price)
        }
    }
    class ItemsTabViewHolder(var binding: ItemsListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {}

    fun submitData(data: List<ItemModel>) = asyncListDiffer.submitList(data)
    object ItemCallback : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
            oldItem == newItem
    }
    private val asyncListDiffer = AsyncListDiffer(this, ItemCallback)
}