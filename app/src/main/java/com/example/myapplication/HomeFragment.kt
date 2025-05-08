package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.animation.AccelerateDecelerateInterpolator

class HomeFragment : Fragment() {
    private var lastCalories = 0
    private var lastProtein = 0
    private var lastFat = 0
    private var lastCarbs = 0
    private var lastBreakfastNorm = 0
    private var lastLunchNorm = 0
    private var lastDinnerNorm = 0
    private var lastSnacksNorm = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateValuesWithAnimation(view, animate = false)
    }

    override fun onResume() {
        super.onResume()
        view?.let { updateValuesWithAnimation(it, animate = true) }
    }

    private fun animateTextViewChange(textView: TextView, oldValue: Int, newValue: Int, suffix: String = "") {
        if (oldValue == newValue) {
            textView.text = "$newValue$suffix"
            return
        }
        val animator = ValueAnimator.ofInt(oldValue, newValue)
        animator.duration = 1000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            textView.text = "${animation.animatedValue}$suffix"
            val scale = 1f + 0.15f * kotlin.math.abs((animation.animatedFraction - 0.5f) * 2)
            textView.scaleX = scale
            textView.scaleY = scale
        }
        animator.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(p0: android.animation.Animator) {}
            override fun onAnimationEnd(p0: android.animation.Animator) {
                textView.scaleX = 1f
                textView.scaleY = 1f
            }
            override fun onAnimationCancel(p0: android.animation.Animator) {}
            override fun onAnimationRepeat(p0: android.animation.Animator) {}
        })
        animator.start()
    }

    private fun updateValuesWithAnimation(view: View, animate: Boolean) {
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

        // Распределение калорий по приёмам пищи
        val (breakfastPercent, lunchPercent, dinnerPercent, snacksPercent) = when (goal) {
            "Сброс веса" -> listOf(0.30, 0.35, 0.20, 0.15)
            "Набор массы" -> listOf(0.25, 0.30, 0.25, 0.20)
            else -> listOf(0.25, 0.30, 0.25, 0.20)
        }
        val breakfastNorm = (calories * breakfastPercent).toInt()
        val lunchNorm = (calories * lunchPercent).toInt()
        val dinnerNorm = (calories * dinnerPercent).toInt()
        val snacksNorm = (calories * snacksPercent).toInt()

        // Получаем текущее потребление по приёмам пищи (если реализовано)
        val currentBreakfast = prefs.getInt("current_breakfast", 0)
        val currentLunch = prefs.getInt("current_lunch", 0)
        val currentDinner = prefs.getInt("current_dinner", 0)
        val currentSnacks = prefs.getInt("current_snacks", 0)

        // Анимация для калорий
        val kcalLeft = view.findViewById<TextView>(R.id.kcalLeft)
        if (animate) animateTextViewChange(kcalLeft, lastCalories, calories) else kcalLeft?.text = calories.toString()
        lastCalories = calories

        // Анимация для макроэлементов
        val carbsView = view.findViewById<TextView>(R.id.carbs)
        if (animate) animateTextViewChange(carbsView, lastCarbs, currentCarbs) else carbsView?.text = currentCarbs.toString()
        lastCarbs = currentCarbs
        val carbsNormView = view.findViewById<TextView>(R.id.carbsNorm)
        carbsNormView?.text = "/ $carbs г"

        val proteinView = view.findViewById<TextView>(R.id.protein)
        if (animate) animateTextViewChange(proteinView, lastProtein, currentProtein) else proteinView?.text = currentProtein.toString()
        lastProtein = currentProtein
        val proteinNormView = view.findViewById<TextView>(R.id.proteinNorm)
        proteinNormView?.text = "/ $protein г"

        val fatView = view.findViewById<TextView>(R.id.fat)
        if (animate) animateTextViewChange(fatView, lastFat, currentFat) else fatView?.text = currentFat.toString()
        lastFat = currentFat
        val fatNormView = view.findViewById<TextView>(R.id.fatNorm)
        fatNormView?.text = "/ $fat г"

        // Анимация для калорий по приёмам пищи
        val breakfastKcalView = view.findViewById<TextView>(R.id.breakfastKcal)
        if (animate) animateTextViewChange(breakfastKcalView, lastBreakfastNorm, currentBreakfast, " / $breakfastNorm ккал")
        else breakfastKcalView?.text = "$currentBreakfast / $breakfastNorm ккал"
        lastBreakfastNorm = currentBreakfast

        val lunchKcalView = view.findViewById<TextView>(R.id.lunchKcal)
        if (animate) animateTextViewChange(lunchKcalView, lastLunchNorm, currentLunch, " / $lunchNorm ккал")
        else lunchKcalView?.text = "$currentLunch / $lunchNorm ккал"
        lastLunchNorm = currentLunch

        val dinnerKcalView = view.findViewById<TextView>(R.id.dinnerKcal)
        if (animate) animateTextViewChange(dinnerKcalView, lastDinnerNorm, currentDinner, " / $dinnerNorm ккал")
        else dinnerKcalView?.text = "$currentDinner / $dinnerNorm ккал"
        lastDinnerNorm = currentDinner

        val snacksKcalView = view.findViewById<TextView>(R.id.snacksKcal)
        if (animate) animateTextViewChange(snacksKcalView, lastSnacksNorm, currentSnacks, " / $snacksNorm ккал")
        else snacksKcalView?.text = "$currentSnacks / $snacksNorm ккал"
        lastSnacksNorm = currentSnacks
    }
} 