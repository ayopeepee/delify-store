package com.swmpire.delifyit.presentation.ui.main.tabs.items.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.protobuf.Internal.ListAdapter
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.ItemsListItemBinding
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.presentation.ui.main.tabs.items.ItemsFragmentDirections
import com.swmpire.delifyit.presentation.ui.main.tabs.items.ItemsViewModel

class ItemsTabListAdapter(
    private val parentFragment: Fragment,
    private val itemsViewModel: ItemsViewModel
) :
    RecyclerView.Adapter<ItemsTabListAdapter.ItemsTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsTabViewHolder {
        val binding =
            ItemsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsTabViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemsTabViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]

        with(holder.binding) {
            apply {
                Glide.with(holder.itemView.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageViewItem)

                textViewName.text = item.name
                textViewDescription.text = item.description
                textViewPrice.text =
                    holder.itemView.context.getString(R.string.price_tenge, item.price)
            }


            buttonChangeItem.setOnClickListener {
                parentFragment.findNavController()
                    .navigate(ItemsFragmentDirections.actionItemsFragmentToChangeItemFragment(item))
            }
            cardViewHolder.setOnLongClickListener {
                when (cardViewHolder.isChecked) {
                    true -> {
                        itemsViewModel.setIsSelected(item.id, false)
                        cardViewHolder.isChecked = false
                    }

                    false -> {
                        itemsViewModel.setIsSelected(item.id, true)
                        cardViewHolder.isChecked = true
                        isAnySelected = true
                    }
                }
                true
            }
            cardViewHolder.setOnClickListener {
                when (cardViewHolder.isChecked) {
                    true -> {
                        cardViewHolder.isChecked = false
                        itemsViewModel.setIsSelected(item.id, false)
                    }

                    false -> {
                        if (isAnySelected) {
                            cardViewHolder.isChecked = true
                            itemsViewModel.setIsSelected(item.id, true)
                        }
                    }
                }
            }
        }

    }

    class ItemsTabViewHolder(var binding: ItemsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    fun submitData(data: List<ItemModel>) = asyncListDiffer.submitList(data)

    fun deselectAll() {
        itemsViewModel.deselectAllItems()
        asyncListDiffer.currentList.forEachIndexed { index, itemModel ->
            notifyItemChanged(index)
        }
        isAnySelected = false
    }

    object ItemCallback : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
            oldItem == newItem
    }

    private val asyncListDiffer = AsyncListDiffer(this, ItemCallback)
    private var isAnySelected: Boolean = false
}