package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import android.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.graphics.Color
import android.widget.TextView

class ProfileFragment : Fragment() {
    private var userName: String = ""
    private var weight: String = ""
    private var height: String = ""
    private var age: String = ""
    private var gender: String = ""
    private var lifestyle: String = ""
    private var goal: String = ""

    private var hasUnsavedChanges = false
    private var isEditMode = false

    private lateinit var ageUnit: TextView
    private lateinit var userNameEdit: EditText
    private lateinit var weightEdit: EditText
    private lateinit var heightEdit: EditText
    private lateinit var ageEdit: EditText
    private lateinit var genderSpinner: MaterialAutoCompleteTextView
    private lateinit var lifestyleSpinner: MaterialAutoCompleteTextView
    private lateinit var goalSpinner: MaterialAutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genderArray = resources.getStringArray(R.array.gender_array)
        val lifestyleArray = resources.getStringArray(R.array.lifestyle_array)
        val goalArray = resources.getStringArray(R.array.goal_array)

        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genderArray)
        val lifestyleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, lifestyleArray)
        val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, goalArray)

        genderSpinner = view.findViewById(R.id.genderSpinner)
        lifestyleSpinner = view.findViewById(R.id.lifestyleSpinner)
        goalSpinner = view.findViewById(R.id.goalSpinner)
        userNameEdit = view.findViewById(R.id.userNameEdit)
        weightEdit = view.findViewById(R.id.weightEdit)
        heightEdit = view.findViewById(R.id.heightEdit)
        ageEdit = view.findViewById(R.id.ageEdit)
        ageUnit = view.findViewById(R.id.ageUnit)

        genderSpinner.setAdapter(genderAdapter)
        lifestyleSpinner.setAdapter(lifestyleAdapter)
        goalSpinner.setAdapter(goalAdapter)

        // SharedPreferences для профиля
        val sharedPrefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

        // Загружаем сохранённые значения
        userName = sharedPrefs.getString("userName", "") ?: ""
        weight = sharedPrefs.getString("weight", "") ?: ""
        height = sharedPrefs.getString("height", "") ?: ""
        age = sharedPrefs.getString("age", "") ?: ""
        gender = sharedPrefs.getString("gender", "") ?: ""
        lifestyle = sharedPrefs.getString("lifestyle", "") ?: ""
        goal = sharedPrefs.getString("goal", "") ?: ""

        // Устанавливаем значения в поля
        userNameEdit.setText(userName)
        weightEdit.setText(weight)
        heightEdit.setText(height)
        ageEdit.setText(age)
        genderSpinner.setText(gender, false)
        lifestyleSpinner.setText(lifestyle, false)
        goalSpinner.setText(goal, false)
        // Обновляем подпись возраста
        ageUnit.text = getAgeUnit(age.toIntOrNull() ?: 0)

        // Отключаем редактирование полей
        setFieldsEditable(false)

        // Показываем кнопку редактирования
        showEditButton()

        // Обработчик нажатия на кнопку редактирования
        activity?.findViewById<AppCompatImageButton>(R.id.editProfileButton)?.setOnClickListener {
            isEditMode = true
            setFieldsEditable(true)
            hideEditButton()
            android.widget.Toast.makeText(requireContext(), "Редактирование профиля", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Обработчик нажатия на кнопку сохранения
        activity?.findViewById<AppCompatImageButton>(R.id.saveProfileButton)?.setOnClickListener {
            saveProfile()
            isEditMode = false
            setFieldsEditable(false)
            showEditButton()
        }

        // Обработчик нажатия на кнопку Мои аллергены
        view.findViewById<View>(R.id.btnAllergens)?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AllergensFragment())
                .addToBackStack(null)
                .commit()
        }

        // Отключаем появление клавиатуры при нажатии на выпадающие списки
        fun disableKeyboard(autoComplete: MaterialAutoCompleteTextView) {
            autoComplete.inputType = 0
            autoComplete.keyListener = null
            autoComplete.isCursorVisible = false
            autoComplete.setOnTouchListener { v, event ->
                if (!isEditMode) return@setOnTouchListener true
                v.performClick()
                autoComplete.showDropDown()
                v.clearFocus()
                true
            }
        }
        disableKeyboard(genderSpinner)
        disableKeyboard(lifestyleSpinner)
        disableKeyboard(goalSpinner)

        // Показываем галочку при изменении любого поля
        val userNameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showSaveButton()
                hasUnsavedChanges = true
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        val weightWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showSaveButton()
                hasUnsavedChanges = true
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        val heightWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showSaveButton()
                hasUnsavedChanges = true
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        val ageWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val value = s.toString().toIntOrNull() ?: 0
                ageUnit.text = getAgeUnit(value)
                showSaveButton()
                hasUnsavedChanges = true
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        userNameEdit.addTextChangedListener(userNameWatcher)
        weightEdit.addTextChangedListener(weightWatcher)
        heightEdit.addTextChangedListener(heightWatcher)
        ageEdit.addTextChangedListener(ageWatcher)

        genderSpinner.setOnItemClickListener { _, _, _, _ -> 
            showSaveButton()
            hasUnsavedChanges = true
        }
        lifestyleSpinner.setOnItemClickListener { _, _, _, _ -> 
            showSaveButton()
            hasUnsavedChanges = true
        }
        goalSpinner.setOnItemClickListener { _, _, _, _ -> 
            showSaveButton()
            hasUnsavedChanges = true
        }
    }

    private fun setFieldsEditable(editable: Boolean) {
        userNameEdit.isEnabled = editable
        weightEdit.isEnabled = editable
        heightEdit.isEnabled = editable
        ageEdit.isEnabled = editable
        genderSpinner.isEnabled = editable
        lifestyleSpinner.isEnabled = editable
        goalSpinner.isEnabled = editable
    }

    private fun showEditButton() {
        try {
            activity?.let { activity ->
                activity.findViewById<AppCompatImageButton>(R.id.editProfileButton)?.let { button ->
                    button.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideEditButton() {
        try {
            activity?.let { activity ->
                activity.findViewById<AppCompatImageButton>(R.id.editProfileButton)?.let { button ->
                    button.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showSaveButton() {
        try {
            activity?.let { activity ->
                activity.findViewById<AppCompatImageButton>(R.id.saveProfileButton)?.let { button ->
                    button.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideSaveButton() {
        try {
            activity?.let { activity ->
                activity.findViewById<AppCompatImageButton>(R.id.saveProfileButton)?.let { button ->
                    button.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveProfile() {
        try {
            view?.let { v ->
                // Получаем текущие значения из полей
                val userName = v.findViewById<EditText>(R.id.userNameEdit)?.text.toString()
                val weight = v.findViewById<EditText>(R.id.weightEdit)?.text.toString()
                val height = v.findViewById<EditText>(R.id.heightEdit)?.text.toString()
                val age = v.findViewById<EditText>(R.id.ageEdit)?.text.toString()
                val gender = v.findViewById<MaterialAutoCompleteTextView>(R.id.genderSpinner)?.text.toString()
                val lifestyle = v.findViewById<MaterialAutoCompleteTextView>(R.id.lifestyleSpinner)?.text.toString()
                val goal = v.findViewById<MaterialAutoCompleteTextView>(R.id.goalSpinner)?.text.toString()

                // Проверка диапазона веса
                val weightNum = weight.toIntOrNull()
                if (weightNum == null || weightNum < 20 || weightNum > 400) {
                    val dialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Некорректный вес")
                        .setMessage("Введите вес от 20 до 400 кг.")
                        .setPositiveButton("ОК") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                    dialog.setOnShowListener {
                        val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positive.setBackgroundColor(Color.parseColor("#E53935")) // красный
                        positive.setTextColor(Color.WHITE)
                        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_card)
                    }
                    dialog.show()
                    return
                }

                // Проверка диапазона роста
                val heightNum = height.toIntOrNull()
                if (heightNum == null || heightNum < 80 || heightNum > 300) {
                    val dialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Некорректный рост")
                        .setMessage("Введите рост от 80 до 300 см.")
                        .setPositiveButton("ОК") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                    dialog.setOnShowListener {
                        val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positive.setBackgroundColor(Color.parseColor("#E53935")) // красный
                        positive.setTextColor(Color.WHITE)
                        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_card)
                    }
                    dialog.show()
                    return
                }

                // Проверка диапазона возраста
                val ageNum = age.toIntOrNull()
                if (ageNum == null || ageNum < 14 || ageNum > 130) {
                    val dialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Некорректный возраст")
                        .setMessage("Введите возраст от 14 до 130 лет.")
                        .setPositiveButton("ОК") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                    dialog.setOnShowListener {
                        val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positive.setBackgroundColor(Color.parseColor("#E53935")) // красный
                        positive.setTextColor(Color.WHITE)
                        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_card)
                    }
                    dialog.show()
                    return
                }

                // Сохраняем в SharedPreferences
                val sharedPrefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.putString("userName", userName)
                editor.putString("weight", weight)
                editor.putString("height", height)
                editor.putString("age", age)
                editor.putString("gender", gender)
                editor.putString("lifestyle", lifestyle)
                editor.putString("goal", goal)
                editor.putBoolean("has_unsaved_changes", false)
                editor.commit()

                // Обновляем подпись возраста после сохранения
                ageUnit.text = getAgeUnit(age.toIntOrNull() ?: 0)

                // Скрываем галочку
                hideSaveButton()
                hasUnsavedChanges = false

                // Показываем уведомление о сохранении
                android.widget.Toast.makeText(requireContext(), "Сохранено", android.widget.Toast.LENGTH_SHORT).show()

                // Обновляем страницу
                (activity as? MainActivity)?.loadFragment(ProfileFragment())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAgeUnit(age: Int): String {
        val lastDigit = age % 10
        val lastTwoDigits = age % 100
        return when {
            lastTwoDigits in 11..14 -> "лет"
            lastDigit == 1 -> "год"
            lastDigit in 2..4 -> "года"
            else -> "лет"
        }
    }

    override fun onPause() {
        super.onPause()
        hideSaveButton()
        hideEditButton()
    }
} 