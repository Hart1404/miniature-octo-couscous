package com.example.myapplication.adapters

import android.graphics.Color
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
        val saltTextView: TextView = view.findViewById(R.id.productCardSalt)
        val calciumTextView: TextView = view.findViewById(R.id.productCardCalcium)
        val magnesiumTextView: TextView = view.findViewById(R.id.productCardMagnesium)
        val potassiumTextView: TextView = view.findViewById(R.id.productCardPotassium)
        val ironTextView: TextView = view.findViewById(R.id.productCardIron)
        val fiberTextView: TextView = view.findViewById(R.id.productCardFiber)
        val omega3TextView: TextView = view.findViewById(R.id.productCardOmega3)
        val vitaminDTextView: TextView = view.findViewById(R.id.productCardVitaminD)
        val vitaminCTextView: TextView = view.findViewById(R.id.productCardVitaminC)
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
        holder.saltTextView.text = "%.2f".format(product.salt)
        holder.calciumTextView.text = product.calcium.toString()
        holder.magnesiumTextView.text = product.magnesium.toString()
        holder.potassiumTextView.text = product.potassium.toString()
        holder.ironTextView.text = "%.2f".format(product.iron)
        holder.fiberTextView.text = "%.2f".format(product.fiber)
        holder.omega3TextView.text = "%.2f".format(product.omega3)
        holder.vitaminDTextView.text = product.vitaminD.toString()
        holder.vitaminCTextView.text = product.vitaminC.toString()
        holder.saltTextView.setTextColor(Color.parseColor("#F39C12"))
        holder.calciumTextView.setTextColor(Color.parseColor("#3498DB"))
        holder.magnesiumTextView.setTextColor(Color.parseColor("#2ECC71"))
        holder.potassiumTextView.setTextColor(Color.parseColor("#1ABC9C"))
        holder.ironTextView.setTextColor(Color.parseColor("#E67E22"))
        holder.fiberTextView.setTextColor(Color.parseColor("#8E44AD"))
        holder.omega3TextView.setTextColor(Color.parseColor("#16A085"))
        holder.vitaminDTextView.setTextColor(Color.parseColor("#2980B9"))
        holder.vitaminCTextView.setTextColor(Color.parseColor("#27AE60"))
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