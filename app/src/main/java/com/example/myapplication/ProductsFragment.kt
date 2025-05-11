package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductNamesAdapter
import com.example.myapplication.adapters.ProductCardAdapter
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

class ProductsFragment : Fragment() {
    private var _view: View? = null
    private var productNamesRecyclerView: RecyclerView? = null
    private var titleTextView: TextView? = null
    private var productNamesAdapter: ProductNamesAdapter? = null
    private var mealType: String? = null
    private var selectedProduct: Product? = null
    private var selectedProductIndex: Int = RecyclerView.NO_POSITION
    private var detailsButton: Button? = null
    private var productNamesCard: View? = null
    private var allProducts: List<Product> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_products, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        detailsButton = view.findViewById(R.id.selectedProductDetailsButton)
        detailsButton?.setBackgroundResource(R.drawable.rounded_button_green)
        detailsButton?.setTextColor(resources.getColor(R.color.white))
        detailsButton?.setOnClickListener {
            selectedProduct?.let { product ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ProductDetailsFragment.newInstance(product))
                    .addToBackStack(null)
                    .commit()
            }
        }

        productNamesRecyclerView = view.findViewById(R.id.productNamesRecyclerView)
        productNamesRecyclerView?.layoutManager = LinearLayoutManager(context)
        productNamesAdapter = ProductNamesAdapter(emptyList(), selectedProductIndex) { product, index ->
            selectedProduct = product
            selectedProductIndex = index
            productNamesAdapter?.setSelectedIndex(index)
            detailsButton?.visibility = View.VISIBLE
        }
        productNamesRecyclerView?.adapter = productNamesAdapter

        titleTextView = view.findViewById(R.id.titleTextView)
        
        mealType = arguments?.getString("mealType")
        val mealTypeRu = when (mealType) {
            "breakfast" -> "завтрака"
            "lunch" -> "обеда"
            "dinner" -> "ужина"
            "snacks" -> "перекуса"
            else -> mealType ?: ""
        }
        titleTextView?.text = "Продукты для $mealTypeRu"

        productNamesCard = view.findViewById(R.id.productNamesCard)
        productNamesCard?.visibility = View.GONE
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        searchEditText.doAfterTextChanged { text ->
            val query = text?.toString()?.trim() ?: ""
            if (query.isEmpty()) {
                productNamesAdapter?.updateProducts(emptyList())
                productNamesCard?.visibility = View.GONE
                detailsButton?.visibility = View.GONE
            } else {
                val filtered = allProducts.filter { it.name.contains(query, ignoreCase = true) }
                productNamesAdapter?.updateProducts(filtered)
                productNamesCard?.visibility = if (filtered.isNotEmpty()) View.VISIBLE else View.GONE
                detailsButton?.visibility = View.GONE
            }
        }

        loadProducts()
    }

    private fun loadProducts() {
        if (!isAdded) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = AppDatabase.getDatabase(requireContext())
                val products = database.productDao().getAll()
                allProducts = products
                Log.d("ProductsFragment", "Loaded products: ${products.size}")
                withContext(Dispatchers.Main) {
                    productNamesAdapter?.updateProducts(emptyList())
                    productNamesCard?.visibility = View.GONE
                    selectedProduct = null
                    selectedProductIndex = RecyclerView.NO_POSITION
                    productNamesAdapter?.setSelectedIndex(RecyclerView.NO_POSITION)
                    detailsButton?.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("ProductsFragment", "Error loading products", e)
            }
        }
    }

    private fun saveNutritionInfo(product: Product) {
        if (!isAdded) return

        val sharedPrefs = requireContext().getSharedPreferences("NutritionPrefs", 0)
        val editor = sharedPrefs.edit()

        editor.putString("selectedProduct", product.name)
        editor.putInt("calories", product.calories)
        editor.putFloat("protein", product.protein)
        editor.putFloat("fat", product.fat)
        editor.putFloat("carbs", product.carbs)
        editor.apply()

        if (isAdded) {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
        productNamesRecyclerView = null
        titleTextView = null
        productNamesAdapter = null
        detailsButton = null
        productNamesCard = null
        allProducts = emptyList()
    }
} 