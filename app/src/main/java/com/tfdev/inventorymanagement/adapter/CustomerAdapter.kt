package com.tfdev.inventorymanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tfdev.inventorymanagement.data.Customer

class CustomerAdapter : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(
    object : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(old: Customer, new: Customer) = old.customerId == new.customerId
        override fun areContentsTheSame(old: Customer, new: Customer) = old == new
    }
) {
    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val item = getItem(position)
        holder.text.text = "${item.name} - ${item.city}"
    }
}
