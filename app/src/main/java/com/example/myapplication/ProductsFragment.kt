package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductNamesAdapter
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsFragment : Fragment() {
    private var _view: View? = null
    private var productsRecyclerView: RecyclerView? = null
    private var productNamesRecyclerView: RecyclerView? = null
    private var titleTextView: TextView? = null
    private var productAdapter: ProductAdapter? = null
    private var productNamesAdapter: ProductNamesAdapter? = null
    private var mealType: String? = null

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
        
        // Инициализация RecyclerView для полного списка продуктов
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView)
        productsRecyclerView?.layoutManager = LinearLayoutManager(context)
        productAdapter = ProductAdapter(emptyList()) { product ->
            saveNutritionInfo(product)
        }
        productsRecyclerView?.adapter = productAdapter

        // Инициализация RecyclerView для списка названий
        productNamesRecyclerView = view.findViewById(R.id.productNamesRecyclerView)
        productNamesRecyclerView?.layoutManager = LinearLayoutManager(context)
        productNamesAdapter = ProductNamesAdapter(emptyList())
        productNamesRecyclerView?.adapter = productNamesAdapter

        titleTextView = view.findViewById(R.id.titleTextView)
        
        // Получаем тип приема пищи из аргументов
        mealType = arguments?.getString("mealType")
        titleTextView?.text = "Продукты для $mealType"

        // Загружаем продукты
        loadProducts()
    }

    private fun loadProducts() {
        if (!isAdded) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = AppDatabase.getDatabase(requireContext())
                val products = database.productDao().getAll()
                Log.d("ProductsFragment", "Loaded products: ${products.size}")
                withContext(Dispatchers.Main) {
                    productAdapter?.updateProducts(products)
                    productNamesAdapter?.updateProducts(products)
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

        // Сохраняем информацию о продукте
        editor.putString("selectedProduct", product.Title)
        editor.putInt("calories", product.Calories)
        editor.putInt("protein", product.Protein)
        editor.putInt("fat", product.Fat)
        editor.putInt("carbs", product.Carbohydrates)
        editor.putFloat("salt", product.Salt.toFloat())
        editor.putInt("calcium", product.Calcium)
        editor.putInt("magnesium", product.Magnesium)
        editor.putInt("potassium", product.Potassium)
        editor.putFloat("iron", product.Iron)
        editor.putFloat("fiber", product.Fiber)
        editor.putFloat("omega3", product.Omega_3)
        editor.putInt("vitaminD", product.Vitamin_D)
        editor.putFloat("vitaminC", product.Vitamin_C)
        editor.apply()

        // Возвращаемся на предыдущий экран
        if (isAdded) {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
        productsRecyclerView = null
        productNamesRecyclerView = null
        titleTextView = null
        productAdapter = null
        productNamesAdapter = null
    }
} 