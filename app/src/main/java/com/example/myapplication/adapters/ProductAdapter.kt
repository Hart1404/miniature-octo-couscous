package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Product

class ProductAdapter(
    private var products: List<Product> = emptyList(),
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.productTitleTextView)
        val caloriesTextView: TextView = view.findViewById(R.id.caloriesTextView)
        val proteinTextView: TextView = view.findViewById(R.id.proteinTextView)
        val fatTextView: TextView = view.findViewById(R.id.fatTextView)
        val carbsTextView: TextView = view.findViewById(R.id.carbsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.titleTextView.text = product.Title
        holder.caloriesTextView.text = "Калории: ${product.Calories}"
        holder.proteinTextView.text = "Белки: ${product.Protein}г"
        holder.fatTextView.text = "Жиры: ${product.Fat}г"
        holder.carbsTextView.text = "Углеводы: ${product.Carbohydrates}г"

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
} 