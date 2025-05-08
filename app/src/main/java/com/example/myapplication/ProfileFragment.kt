package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ProfileFragment : Fragment() {
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

        genderSpinner.setAdapter(genderAdapter)
        lifestyleSpinner.setAdapter(lifestyleAdapter)
        goalSpinner.setAdapter(goalAdapter)

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
    }
} 