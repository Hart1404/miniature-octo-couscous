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
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var lastCalories = 0
    private var lastProtein = 0
    private var lastFat = 0
    private var lastCarbs = 0
    private var lastBreakfastNorm = 0
    private var lastLunchNorm = 0
    private var lastDinnerNorm = 0
    private var lastSnacksNorm = 0
    private var lastSalt = 0.0
    private var lastCalcium = 0
    private var lastMagnesium = 0
    private var lastPotassium = 0
    private var lastIron = 0
    private var lastFiber = 0.0
    private var lastOmega3 = 0.0
    private var lastVitaminD = 0
    private var lastVitaminC = 0

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

        // Устанавливаем текущую дату
        val dateText = view.findViewById<TextView>(R.id.dateText)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d MMM", Locale("ru"))
        val today = dateFormat.format(calendar.time)
        dateText.text = "Сегодня, $today"

        // Переход в ActivityFragment по нажатию на зелёную кнопку
        view.findViewById<View>(R.id.fab_activity)?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ActivityFragment())
                .addToBackStack(null)
                .commit()
        }
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
        val caloriesBase = tdee.toInt()

        // Получаем текущее потребление калорий (съедено)
        val currentEaten = prefs.getInt("current_eaten", 0)
        view.findViewById<TextView>(R.id.eatenKcal)?.text = currentEaten.toString()

        // Получаем количество шагов для расчёта сожжённых калорий
        val stepsPrefs = requireContext().getSharedPreferences("activity_prefs", Context.MODE_PRIVATE)
        val steps = stepsPrefs.getString("steps", "0") ?: "0"
        view.findViewById<TextView>(R.id.walking)?.text = steps
        view.findViewById<TextView>(R.id.walkingLabel)?.text = getStepsLabel(steps)
        val stepsNum = steps.toIntOrNull() ?: 0
        val heightM = height / 100.0
        val burned = (weight * stepsNum * heightM * 0.000315).toInt()
        view.findViewById<TextView>(R.id.burnedKcal)?.text = burned.toString()

        // Оставшиеся калории = caloriesBase - currentEaten + burned
        val kcalLeft = view.findViewById<TextView>(R.id.kcalLeft)
        val left = caloriesBase - currentEaten + burned
        if (animate) animateTextViewChange(kcalLeft, lastCalories, left) else kcalLeft?.text = left.toString()
        lastCalories = left

        // Калорийность на день = базовые калории + сожжённые калории
        val caloriesForDay = caloriesBase + burned

        // 4. Белки, жиры, углеводы по процентам (от caloriesForDay)
        val (proteinPercent, fatPercent, carbsPercent) = when (goal) {
            "Сброс веса" -> Triple(0.45, 0.25, 0.30)
            "Набор массы" -> Triple(0.30, 0.20, 0.50)
            else -> Triple(0.30, 0.30, 0.40)
        }
        val protein = ((caloriesForDay * proteinPercent) / 4).toInt()
        val fat = ((caloriesForDay * fatPercent) / 9).toInt()
        val carbs = ((caloriesForDay * carbsPercent) / 4).toInt()

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
        val breakfastNorm = (caloriesForDay * breakfastPercent).toInt()
        val lunchNorm = (caloriesForDay * lunchPercent).toInt()
        val dinnerNorm = (caloriesForDay * dinnerPercent).toInt()
        val snacksNorm = (caloriesForDay * snacksPercent).toInt()

        // Получаем текущее потребление по приёмам пищи (если реализовано)
        val currentBreakfast = prefs.getInt("current_breakfast", 0)
        val currentLunch = prefs.getInt("current_lunch", 0)
        val currentDinner = prefs.getInt("current_dinner", 0)
        val currentSnacks = prefs.getInt("current_snacks", 0)

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

        // Расчет норм макроэлементов
        // Соль (г)
        val saltNorm = (3 + (weight * 0.01) + (activityFactor * 0.3) - (age * 0.01)).coerceIn(3.0, 6.0)
        
        // Кальций (мг)
        val calciumNorm = 1000 + (if (age >= 50) 200 else 0) + (if (gender == "Женский") 200 else 0)
        
        // Магний (мг)
        val magnesiumNorm = (if (gender == "Мужской") 400 else 300) + (weight * 0.5) + (activityFactor * 20)
        
        // Калий (мг)
        val potassiumNorm = 2500 + (weight * 10) + (height * 2) - (age * 5)
        
        // Железо (мг)
        val ironNorm = (if (gender == "Женский") 18 else 8) + (if (goal == "Набор массы") 2 else 0)
        
        // Клетчатка (г)
        val fiberNorm = weight * (if (goal == "Сброс веса") 0.5 else 0.4)
        
        // Омега-3 (г)
        val omega3Norm = 1 + (activityFactor * 0.5) + (if (goal == "Сброс веса") 0.3 else 0.0)
        
        // Витамин D (МЕ)
        val vitaminDNorm = 600 + (if (age >= 50) 200 else 0) + (if (activityFactor >= 1.55) 200 else 0)
        
        // Витамин C (мг)
        val vitaminCNorm = (if (gender == "Мужской") 90 else 75) + (activityFactor * 10)

        // Получаем текущее потребление макроэлементов
        val currentSalt = prefs.getFloat("current_salt", 0f).toDouble()
        val currentCalcium = prefs.getInt("current_calcium", 0)
        val currentMagnesium = prefs.getInt("current_magnesium", 0)
        val currentPotassium = prefs.getInt("current_potassium", 0)
        val currentIron = prefs.getInt("current_iron", 0)
        val currentFiber = prefs.getFloat("current_fiber", 0f).toDouble()
        val currentOmega3 = prefs.getFloat("current_omega3", 0f).toDouble()
        val currentVitaminD = prefs.getInt("current_vitamin_d", 0)
        val currentVitaminC = prefs.getInt("current_vitamin_c", 0)

        // Анимация для макроэлементов
        val saltView = view.findViewById<TextView>(R.id.salt)
        if (animate) animateTextViewChange(saltView, lastSalt.toInt(), currentSalt.toInt()) else saltView?.text = currentSalt.toInt().toString()
        lastSalt = currentSalt
        val saltNormView = view.findViewById<TextView>(R.id.saltNorm)
        saltNormView?.text = "/ ${saltNorm.toInt()} г"

        val calciumView = view.findViewById<TextView>(R.id.calcium)
        if (animate) animateTextViewChange(calciumView, lastCalcium, currentCalcium) else calciumView?.text = currentCalcium.toString()
        lastCalcium = currentCalcium
        val calciumNormView = view.findViewById<TextView>(R.id.calciumNorm)
        calciumNormView?.text = "/ $calciumNorm мг"

        val magnesiumView = view.findViewById<TextView>(R.id.magnesium)
        if (animate) animateTextViewChange(magnesiumView, lastMagnesium, currentMagnesium) else magnesiumView?.text = currentMagnesium.toString()
        lastMagnesium = currentMagnesium
        val magnesiumNormView = view.findViewById<TextView>(R.id.magnesiumNorm)
        magnesiumNormView?.text = "/ ${magnesiumNorm.toInt()} мг"

        val potassiumView = view.findViewById<TextView>(R.id.potassium)
        if (animate) animateTextViewChange(potassiumView, lastPotassium, currentPotassium) else potassiumView?.text = currentPotassium.toString()
        lastPotassium = currentPotassium
        val potassiumNormView = view.findViewById<TextView>(R.id.potassiumNorm)
        potassiumNormView?.text = "/ ${potassiumNorm.toInt()} мг"

        val ironView = view.findViewById<TextView>(R.id.iron)
        if (animate) animateTextViewChange(ironView, lastIron, currentIron) else ironView?.text = currentIron.toString()
        lastIron = currentIron
        val ironNormView = view.findViewById<TextView>(R.id.ironNorm)
        ironNormView?.text = "/ $ironNorm мг"

        val fiberView = view.findViewById<TextView>(R.id.fiber)
        if (animate) animateTextViewChange(fiberView, lastFiber.toInt(), currentFiber.toInt()) else fiberView?.text = currentFiber.toInt().toString()
        lastFiber = currentFiber
        val fiberNormView = view.findViewById<TextView>(R.id.fiberNorm)
        fiberNormView?.text = "/ ${fiberNorm.toInt()} г"

        val omega3View = view.findViewById<TextView>(R.id.omega3)
        if (animate) animateTextViewChange(omega3View, lastOmega3.toInt(), currentOmega3.toInt()) else omega3View?.text = currentOmega3.toInt().toString()
        lastOmega3 = currentOmega3
        val omega3NormView = view.findViewById<TextView>(R.id.omega3Norm)
        omega3NormView?.text = "/ ${omega3Norm.toInt()} г"

        val vitaminDView = view.findViewById<TextView>(R.id.vitaminD)
        if (animate) animateTextViewChange(vitaminDView, lastVitaminD, currentVitaminD) else vitaminDView?.text = currentVitaminD.toString()
        lastVitaminD = currentVitaminD
        val vitaminDNormView = view.findViewById<TextView>(R.id.vitaminDNorm)
        vitaminDNormView?.text = "/ $vitaminDNorm МЕ"

        val vitaminCView = view.findViewById<TextView>(R.id.vitaminC)
        if (animate) animateTextViewChange(vitaminCView, lastVitaminC, currentVitaminC) else vitaminCView?.text = currentVitaminC.toString()
        lastVitaminC = currentVitaminC
        val vitaminCNormView = view.findViewById<TextView>(R.id.vitaminCNorm)
        vitaminCNormView?.text = "/ ${vitaminCNorm.toInt()} мг"
    }

    private fun getStepsLabel(steps: String): String {
        val n = steps.toIntOrNull() ?: 0
        val lastDigit = n % 10
        val lastTwoDigits = n % 100
        return when {
            lastTwoDigits in 11..14 -> "шагов"
            lastDigit == 1 -> "шаг"
            lastDigit in 2..4 -> "шага"
            else -> "шагов"
        }
    }
} 