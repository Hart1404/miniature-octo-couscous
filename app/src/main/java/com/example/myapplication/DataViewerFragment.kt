package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DataViewerFragment : Fragment() {
    private var selectedDate: Date = Date()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DataViewerFragment", "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("DataViewerFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_data_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DataViewerFragment", "onViewCreated called")

        // Устанавливаем начальную дату
        updateDateButton()

        // Обработчик нажатия на кнопку выбора даты
        view.findViewById<View>(R.id.datePickerButton)?.setOnClickListener {
            Log.d("DataViewerFragment", "Date picker button clicked")
            showDatePicker()
        }

        // Загружаем данные для текущей даты
        loadData(selectedDate)
    }

    override fun onStart() {
        super.onStart()
        Log.d("DataViewerFragment", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("DataViewerFragment", "onResume called")
    }

    private fun updateDateButton() {
        Log.d("DataViewerFragment", "updateDateButton called with date: ${dateFormat.format(selectedDate)}")
        view?.findViewById<TextView>(R.id.datePickerButton)?.text = dateFormat.format(selectedDate)
    }

    private fun showDatePicker() {
        Log.d("DataViewerFragment", "showDatePicker called")
        val calendar = Calendar.getInstance().apply { time = selectedDate }
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                Log.d("DataViewerFragment", "Date selected: ${dateFormat.format(selectedDate)}")
                updateDateButton()
                loadData(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadData(date: Date) {
        Log.d("DataViewerFragment", "loadData called for date: ${dateFormat.format(date)}")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = AppDatabase.getDatabase(requireContext())
                Log.d("DataViewerFragment", "Database instance obtained")
                
                val userData = database.userDataDao().getByDate(date)
                Log.d("DataViewerFragment", "Data loaded from database: $userData")
                
                withContext(Dispatchers.Main) {
                    if (userData != null) {
                        Log.d("DataViewerFragment", "UserData is not null, calling displayData")
                        displayData(userData)
                    } else {
                        Log.d("DataViewerFragment", "UserData is null, calling showEmptyData")
                        showEmptyData()
                    }
                }
            } catch (e: Exception) {
                Log.e("DataViewerFragment", "Error loading data", e)
                withContext(Dispatchers.Main) {
                    showError(e.message ?: "Ошибка при загрузке данных")
                }
            }
        }
    }

    private fun displayData(userData: UserData) {
        Log.d("DataViewerFragment", "displayData called with userData: $userData")
        view?.let { view ->
            try {
                // Показываем все контейнеры
                val mainMetricsContainer = view.findViewById<LinearLayout>(R.id.mainMetricsContainer)
                val macronutrientsContainer = view.findViewById<LinearLayout>(R.id.macronutrientsContainer)
                val micronutrientsContainer = view.findViewById<LinearLayout>(R.id.micronutrientsContainer)
                val emptyDataText = view.findViewById<TextView>(R.id.emptyDataText)

                Log.d("DataViewerFragment", "Containers found: mainMetrics=${mainMetricsContainer != null}, " +
                    "macros=${macronutrientsContainer != null}, micros=${micronutrientsContainer != null}")

                mainMetricsContainer?.visibility = View.VISIBLE
                macronutrientsContainer?.visibility = View.VISIBLE
                micronutrientsContainer?.visibility = View.VISIBLE
                emptyDataText?.visibility = View.GONE

                // Основные метрики
                view.findViewById<TextView>(R.id.weightValue)?.apply {
                    text = "${userData.weight} кг"
                    Log.d("DataViewerFragment", "Set weight value: ${userData.weight} кг")
                }
                view.findViewById<TextView>(R.id.caloriesValue)?.apply {
                    text = "${userData.calories} ккал"
                    Log.d("DataViewerFragment", "Set calories value: ${userData.calories} ккал")
                }
                view.findViewById<TextView>(R.id.eatenValue)?.apply {
                    text = "${userData.eaten} ккал"
                    Log.d("DataViewerFragment", "Set eaten value: ${userData.eaten} ккал")
                }
                view.findViewById<TextView>(R.id.burnedValue)?.apply {
                    text = "${userData.burned} ккал"
                    Log.d("DataViewerFragment", "Set burned value: ${userData.burned} ккал")
                }

                // Макронутриенты
                view.findViewById<TextView>(R.id.proteinValue)?.apply {
                    text = "${userData.protein} г"
                    Log.d("DataViewerFragment", "Set protein value: ${userData.protein} г")
                }
                view.findViewById<TextView>(R.id.fatValue)?.apply {
                    text = "${userData.fat} г"
                    Log.d("DataViewerFragment", "Set fat value: ${userData.fat} г")
                }
                view.findViewById<TextView>(R.id.carbsValue)?.apply {
                    text = "${userData.carbs} г"
                    Log.d("DataViewerFragment", "Set carbs value: ${userData.carbs} г")
                }

                // Микронутриенты
                view.findViewById<TextView>(R.id.saltValue)?.apply {
                    text = "${userData.salt} г"
                    Log.d("DataViewerFragment", "Set salt value: ${userData.salt} г")
                }
                view.findViewById<TextView>(R.id.calciumValue)?.apply {
                    text = "${userData.calcium} мг"
                    Log.d("DataViewerFragment", "Set calcium value: ${userData.calcium} мг")
                }
                view.findViewById<TextView>(R.id.magnesiumValue)?.apply {
                    text = "${userData.magnesium} мг"
                    Log.d("DataViewerFragment", "Set magnesium value: ${userData.magnesium} мг")
                }
                view.findViewById<TextView>(R.id.potassiumValue)?.apply {
                    text = "${userData.potassium} мг"
                    Log.d("DataViewerFragment", "Set potassium value: ${userData.potassium} мг")
                }
                view.findViewById<TextView>(R.id.ironValue)?.apply {
                    text = "${userData.iron} мг"
                    Log.d("DataViewerFragment", "Set iron value: ${userData.iron} мг")
                }
                view.findViewById<TextView>(R.id.fiberValue)?.apply {
                    text = "${userData.fiber} г"
                    Log.d("DataViewerFragment", "Set fiber value: ${userData.fiber} г")
                }
                view.findViewById<TextView>(R.id.omega3Value)?.apply {
                    text = "${userData.omega3} г"
                    Log.d("DataViewerFragment", "Set omega3 value: ${userData.omega3} г")
                }
                view.findViewById<TextView>(R.id.vitaminDValue)?.apply {
                    text = "${userData.vitaminD} МЕ"
                    Log.d("DataViewerFragment", "Set vitaminD value: ${userData.vitaminD} МЕ")
                }
                view.findViewById<TextView>(R.id.vitaminCValue)?.apply {
                    text = "${userData.vitaminC} мг"
                    Log.d("DataViewerFragment", "Set vitaminC value: ${userData.vitaminC} мг")
                }
            } catch (e: Exception) {
                Log.e("DataViewerFragment", "Error displaying data", e)
                showError("Ошибка при отображении данных: ${e.message}")
            }
        }
    }

    private fun showEmptyData() {
        Log.d("DataViewerFragment", "showEmptyData called")
        view?.let { view ->
            // Скрываем все контейнеры с данными
            view.findViewById<LinearLayout>(R.id.mainMetricsContainer)?.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.macronutrientsContainer)?.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.micronutrientsContainer)?.visibility = View.GONE

            // Показываем сообщение об отсутствии данных
            view.findViewById<TextView>(R.id.emptyDataText)?.apply {
                visibility = View.VISIBLE
                text = "Нет данных за выбранную дату"
            }
        }
    }

    private fun showError(message: String) {
        Log.e("DataViewerFragment", "showError called with message: $message")
        view?.let { view ->
            // Скрываем все контейнеры с данными
            view.findViewById<LinearLayout>(R.id.mainMetricsContainer)?.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.macronutrientsContainer)?.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.micronutrientsContainer)?.visibility = View.GONE

            // Показываем сообщение об ошибке
            view.findViewById<TextView>(R.id.emptyDataText)?.apply {
                visibility = View.VISIBLE
                text = message
            }
        }
    }
} 