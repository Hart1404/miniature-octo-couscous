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

class ProfileFragment : Fragment() {
    private var userName: String = ""
    private var weight: String = ""
    private var height: String = ""
    private var age: String = ""
    private var gender: String = ""
    private var lifestyle: String = ""
    private var goal: String = ""

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

        val genderSpinner = view.findViewById<MaterialAutoCompleteTextView>(R.id.genderSpinner)
        val lifestyleSpinner = view.findViewById<MaterialAutoCompleteTextView>(R.id.lifestyleSpinner)
        val goalSpinner = view.findViewById<MaterialAutoCompleteTextView>(R.id.goalSpinner)
        val userNameEdit = view.findViewById<EditText>(R.id.userNameEdit)
        val weightEdit = view.findViewById<EditText>(R.id.weightEdit)
        val heightEdit = view.findViewById<EditText>(R.id.heightEdit)
        val ageEdit = view.findViewById<EditText>(R.id.ageEdit)

        genderSpinner.setAdapter(genderAdapter)
        lifestyleSpinner.setAdapter(lifestyleAdapter)
        goalSpinner.setAdapter(goalAdapter)

        // SharedPreferences для профиля
        val sharedPrefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

        // Загружаем сохранённые значения
        userNameEdit.setText(sharedPrefs.getString("userName", ""))
        weightEdit.setText(sharedPrefs.getString("weight", ""))
        heightEdit.setText(sharedPrefs.getString("height", ""))
        ageEdit.setText(sharedPrefs.getString("age", ""))
        genderSpinner.setText(sharedPrefs.getString("gender", ""), false)
        lifestyleSpinner.setText(sharedPrefs.getString("lifestyle", ""), false)
        goalSpinner.setText(sharedPrefs.getString("goal", ""), false)

        // Отключаем появление клавиатуры при нажатии на выпадающие списки
        fun disableKeyboard(autoComplete: MaterialAutoCompleteTextView) {
            autoComplete.inputType = 0
            autoComplete.keyListener = null
            autoComplete.isCursorVisible = false
            autoComplete.setOnTouchListener { v, event ->
                v.performClick()
                autoComplete.showDropDown()
                v.clearFocus()
                true
            }
        }
        disableKeyboard(genderSpinner)
        disableKeyboard(lifestyleSpinner)
        disableKeyboard(goalSpinner)

        // Галочка для сохранения
        val saveButton = requireActivity().findViewById<AppCompatImageButton>(R.id.saveProfileButton)
        fun showSaveButton() { saveButton.visibility = View.VISIBLE }
        fun hideSaveButton() { saveButton.visibility = View.GONE }

        // Показываем галочку при изменении любого поля
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { showSaveButton() }
            override fun afterTextChanged(s: Editable?) {}
        }
        userNameEdit.addTextChangedListener(watcher)
        weightEdit.addTextChangedListener(watcher)
        heightEdit.addTextChangedListener(watcher)
        ageEdit.addTextChangedListener(watcher)
        genderSpinner.setOnItemClickListener { _, _, _, _ -> showSaveButton() }
        lifestyleSpinner.setOnItemClickListener { _, _, _, _ -> showSaveButton() }
        goalSpinner.setOnItemClickListener { _, _, _, _ -> showSaveButton() }

        // Сохраняем данные при нажатии на галочку
        saveButton.setOnClickListener {
            userName = userNameEdit.text.toString()
            weight = weightEdit.text.toString()
            height = heightEdit.text.toString()
            age = ageEdit.text.toString()
            gender = genderSpinner.text.toString()
            lifestyle = lifestyleSpinner.text.toString()
            goal = goalSpinner.text.toString()
            // Сохраняем в SharedPreferences
            sharedPrefs.edit()
                .putString("userName", userName)
                .putString("weight", weight)
                .putString("height", height)
                .putString("age", age)
                .putString("gender", gender)
                .putString("lifestyle", lifestyle)
                .putString("goal", goal)
                .apply()
            hideSaveButton()
        }
    }
} 