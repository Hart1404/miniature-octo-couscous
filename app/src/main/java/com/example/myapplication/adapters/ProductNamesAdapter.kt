package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Product

class ProductNamesAdapter(
    private var products: List<Product> = emptyList(),
    private var selectedIndex: Int = RecyclerView.NO_POSITION,
    private val onProductClick: (Product, Int) -> Unit
) : RecyclerView.Adapter<ProductNamesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.productTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.titleTextView.text = product.name
        holder.titleTextView.setOnClickListener {
            onProductClick(product, position)
        }
        // Выделение выбранного продукта
        holder.titleTextView.setBackgroundColor(
            if (position == selectedIndex) holder.titleTextView.context.getColor(R.color.appBarColor)
            else holder.titleTextView.context.getColor(R.color.white)
        )
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    fun setSelectedIndex(index: Int) {
        val oldIndex = selectedIndex
        selectedIndex = index
        if (oldIndex != RecyclerView.NO_POSITION) notifyItemChanged(oldIndex)
        if (selectedIndex != RecyclerView.NO_POSITION) notifyItemChanged(selectedIndex)
    }
} 