package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val weight = prefs.getString("weight", "")?.toDoubleOrNull() ?: 0.0
        val height = prefs.getString("height", "")?.toDoubleOrNull() ?: 0.0
        val age = prefs.getString("age", "")?.toDoubleOrNull() ?: 0.0
        val gender = prefs.getString("gender", "Мужской") ?: "Мужской"
        val lifestyle = prefs.getString("lifestyle", "Неактивный") ?: "Неактивный"
        val goal = prefs.getString("goal", "Поддержание веса") ?: "Поддержание веса"

        // 1. BMR
        val bmr = if (gender == "Мужской") {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }

        // 2. Коэффициент активности
        val activityFactor = when (lifestyle) {
            "Неактивный" -> 1.2
            "Умеренный" -> 1.375
            "Активный" -> 1.55
            "Очень активный" -> 1.725
            else -> 1.2
        }
        var tdee = bmr * activityFactor

        // 3. Корректировка по цели
        tdee = when (goal) {
            "Сброс веса" -> tdee * 0.9
            "Набор массы" -> tdee * 1.1
            else -> tdee
        }
        val calories = tdee.toInt()

        // 4. Белки, жиры, углеводы по процентам
        val (proteinPercent, fatPercent, carbsPercent) = when (goal) {
            "Сброс веса" -> Triple(0.45, 0.25, 0.30)
            "Набор массы" -> Triple(0.30, 0.20, 0.50)
            else -> Triple(0.30, 0.30, 0.40)
        }
        val protein = ((calories * proteinPercent) / 4).toInt()
        val fat = ((calories * fatPercent) / 9).toInt()
        val carbs = ((calories * carbsPercent) / 4).toInt()

        // Получаем текущее потребление из SharedPreferences
        val currentCarbs = prefs.getInt("current_carbs", 0)
        val currentProtein = prefs.getInt("current_protein", 0)
        val currentFat = prefs.getInt("current_fat", 0)

        // Выводим значения на экран
        view.findViewById<TextView>(R.id.kcalLeft)?.text = calories.toString()
        
        // Обновляем отображение макроэлементов
        view.findViewById<TextView>(R.id.carbs)?.text = currentCarbs.toString()
        view.findViewById<TextView>(R.id.carbsNorm)?.text = "/ $carbs г"
        view.findViewById<TextView>(R.id.protein)?.text = currentProtein.toString()
        view.findViewById<TextView>(R.id.proteinNorm)?.text = "/ $protein г"
        view.findViewById<TextView>(R.id.fat)?.text = currentFat.toString()
        view.findViewById<TextView>(R.id.fatNorm)?.text = "/ $fat г"
    }
} 