package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ActivityFragment : Fragment() {
    private var hasUnsavedChanges = false
    private var steps: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stepsEdit = view.findViewById<EditText>(R.id.stepsEdit)
        val saveStepsButton = view.findViewById<AppCompatImageButton>(R.id.saveStepsButton)

        // SharedPreferences для шагов
        val sharedPrefs = requireContext().getSharedPreferences("activity_prefs", Context.MODE_PRIVATE)
        steps = sharedPrefs.getString("steps", "") ?: ""
        stepsEdit.setText(steps)

        stepsEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hasUnsavedChanges = true
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        saveStepsButton.setOnClickListener {
            val stepsValue = stepsEdit.text.toString().toIntOrNull() ?: 0
            if (stepsValue < 0 || stepsValue > 200000) {
                // Показываем диалоговое окно с закруглением и красной кнопкой
                val dialog = AlertDialog.Builder(requireContext(), R.style.RoundedAlertDialog)
                    .setTitle("Некорректное значение")
                    .setMessage("Введите количество шагов от 0 до 200 000")
                    .setPositiveButton("ОК", null)
                    .create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                }
                dialog.show()
                return@setOnClickListener
            }
            saveSteps(stepsEdit)
        }
    }

    private fun saveSteps(stepsEdit: EditText) {
        try {
            val stepsValue = stepsEdit.text.toString()
            val stepsNum = stepsValue.toIntOrNull()
            if (stepsNum == null || stepsNum < 0) {
                Toast.makeText(requireContext(), "Введите корректное количество шагов", Toast.LENGTH_SHORT).show()
                return
            }
            val sharedPrefs = requireContext().getSharedPreferences("activity_prefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("steps", stepsValue)
            editor.apply()
            hasUnsavedChanges = false
            // Скрыть клавиатуру
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(stepsEdit.windowToken, 0)
            Toast.makeText(requireContext(), "Шаги сохранены!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
    }
} 