package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Product

class ProductCardAdapter(
    private var products: List<Product> = emptyList(),
    private val onDetailsClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.productCardName)
        val caloriesTextView: TextView = view.findViewById(R.id.productCardCalories)
        val proteinTextView: TextView = view.findViewById(R.id.productCardProtein)
        val fatTextView: TextView = view.findViewById(R.id.productCardFat)
        val carbsTextView: TextView = view.findViewById(R.id.productCardCarbs)
        val tagsTextView: TextView = view.findViewById(R.id.productCardTags)
        val allergensTextView: TextView = view.findViewById(R.id.productCardAllergens)
        val detailsButton: Button = view.findViewById(R.id.cardDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.nameTextView.text = product.name
        holder.caloriesTextView.text = "Калории: ${product.calories}"
        holder.proteinTextView.text = "Белки: ${product.protein}г"
        holder.fatTextView.text = "Жиры: ${product.fat}г"
        holder.carbsTextView.text = "Углеводы: ${product.carbs}г"
        holder.tagsTextView.text = getTagsString(product)
        holder.allergensTextView.text = getAllergensString(product)
        holder.detailsButton.setOnClickListener { onDetailsClick(product) }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    private fun getTagsString(p: Product): String {
        val tags = mutableListOf<String>()
        if (p.isProteinTag) tags.add("Белковое 🥩")
        if (p.isFastTag) tags.add("Быстрое ⏱️")
        if (p.isVeganTag) tags.add("Веган 🌱")
        if (p.isVegetarianTag) tags.add("Вегетарианец 🥗")
        if (p.isSeaTag) tags.add("Морское 🐟")
        if (p.isLightTag) tags.add("Лёгкое 💧")
        if (p.isBreakfastTag) tags.add("Завтрак ☀️")
        if (p.isLunchTag) tags.add("Обед 🍲")
        if (p.isDinnerTag) tags.add("Ужин 🌙")
        if (p.isSnackTag) tags.add("Перекус 🍪")
        if (p.isPeanutTag) tags.add("Арахис 🥜")
        if (p.isFruitsTag) tags.add("Фрукты 🍏")
        if (p.isChocolateTag) tags.add("Шоколад 🍫")
        if (p.isBerriesTag) tags.add("Ягоды 🍓")
        if (p.isVegetablesTag) tags.add("Овощи 🥦")
        return if (tags.isNotEmpty()) "Теги: ${tags.joinToString(", ")}" else "Теги: отсутствуют"
    }

    private fun getAllergensString(p: Product): String {
        val allergens = mutableListOf<String>()
        if (!p.isGlutenFree) allergens.add("Глютен")
        if (!p.isLactoseFree) allergens.add("Лактоза")
        if (!p.isNutsFree) allergens.add("Орехи")
        if (!p.isEggsFree) allergens.add("Яйца")
        if (!p.isFishFree) allergens.add("Рыба")
        return if (allergens.isNotEmpty()) "Аллергены: ${allergens.joinToString(", ")}" else "Без аллергенов"
    }
} 