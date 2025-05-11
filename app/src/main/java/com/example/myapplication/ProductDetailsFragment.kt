package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.data.Product
import android.util.Log

class ProductDetailsFragment : Fragment() {
    companion object {
        private const val ARG_PRODUCT = "product"
        fun newInstance(product: Product): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()
            val args = Bundle()
            args.putSerializable(ARG_PRODUCT, product)
            fragment.arguments = args
            return fragment
        }
    }

    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getSerializable(ARG_PRODUCT) as? Product
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ProductDetailsFragment", "onCreateView вызван для продукта: ${product?.name}")
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product?.let { p ->
            view.findViewById<TextView>(R.id.productCardName)?.text = p.name
            view.findViewById<TextView>(R.id.productCardCalories)?.text = "Калории: ${p.calories}"
            view.findViewById<TextView>(R.id.productCardProtein)?.text = "Белки: ${p.protein}г"
            view.findViewById<TextView>(R.id.productCardFat)?.text = "Жиры: ${p.fat}г"
            view.findViewById<TextView>(R.id.productCardCarbs)?.text = "Углеводы: ${p.carbs}г"
            view.findViewById<TextView>(R.id.productCardTags)?.text = getTagsString(p)
            view.findViewById<TextView>(R.id.productCardAllergens)?.text = getAllergensString(p)
        }
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