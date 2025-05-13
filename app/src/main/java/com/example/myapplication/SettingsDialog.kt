package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SettingsDialog(private val activity: MainActivity) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingsDialog", "onCreate called")
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_settings)

        // Настраиваем прозрачный фон
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Находим кнопку закрытия и устанавливаем обработчик
        findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
            Log.d("SettingsDialog", "Close button clicked")
            dismiss()
        }

        // Настраиваем ширину диалога
        window?.let { window ->
            val width = (activity.resources.displayMetrics.widthPixels * 0.9).toInt()
            val params = window.attributes
            params.width = width
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = params
        }

        // Добавляем кнопку для просмотра данных
        val viewDataButton = findViewById<TextView>(R.id.viewDataButton)
        Log.d("SettingsDialog", "View data button found: ${viewDataButton != null}")
        
        viewDataButton?.setOnClickListener {
            Log.d("SettingsDialog", "View data button clicked")
            dismiss()
            Log.d("SettingsDialog", "MainActivity reference: ${activity != null}")
            activity.loadFragment(DataViewerFragment())
        }
    }

    private fun showUserData() {
        Log.d("SettingsDialog", "showUserData called")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userDataList = AppDatabase.getDatabase(activity).userDataDao().getAll()
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                
                val dataText = StringBuilder()
                for (data in userDataList) {
                    dataText.append("Дата: ${dateFormat.format(data.date)}\n")
                    dataText.append("Вес: ${data.weight} кг\n")
                    dataText.append("Калории: ${data.calories} ккал\n")
                    dataText.append("Белки: ${data.protein} г\n")
                    dataText.append("Жиры: ${data.fat} г\n")
                    dataText.append("Углеводы: ${data.carbs} г\n")
                    dataText.append("Съедено: ${data.eaten} ккал\n")
                    dataText.append("Сожжено: ${data.burned} ккал\n")
                    dataText.append("Соль: ${data.salt} г\n")
                    dataText.append("Кальций: ${data.calcium} мг\n")
                    dataText.append("Магний: ${data.magnesium} мг\n")
                    dataText.append("Калий: ${data.potassium} мг\n")
                    dataText.append("Железо: ${data.iron} мг\n")
                    dataText.append("Клетчатка: ${data.fiber} г\n")
                    dataText.append("Омега-3: ${data.omega3} г\n")
                    dataText.append("Витамин D: ${data.vitaminD} МЕ\n")
                    dataText.append("Витамин C: ${data.vitaminC} мг\n")
                    dataText.append("-------------------\n")
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, dataText.toString(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("SettingsDialog", "Error loading user data", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Ошибка при получении данных: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
} 