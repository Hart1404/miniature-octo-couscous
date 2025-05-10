package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment

class FilterFragment : Fragment() {
    private var hasUnsavedChanges = false
    private var protein = false
    private var fast = false
    private var vegan = false
    private var vegetarian = false
    private var sea = false
    private var light = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireContext().getSharedPreferences("filter_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        // Получаем сохранённые значения
        protein = sharedPrefs.getBoolean("protein", false)
        fast = sharedPrefs.getBoolean("fast", false)
        vegan = sharedPrefs.getBoolean("vegan", false)
        vegetarian = sharedPrefs.getBoolean("vegetarian", false)
        sea = sharedPrefs.getBoolean("sea", false)
        light = sharedPrefs.getBoolean("light", false)

        val switchProtein = view.findViewById<Switch>(R.id.switchProtein)
        val switchFast = view.findViewById<Switch>(R.id.switchFast)
        val switchVegan = view.findViewById<Switch>(R.id.switchVegan)
        val switchVegetarian = view.findViewById<Switch>(R.id.switchVegetarian)
        val switchSea = view.findViewById<Switch>(R.id.switchSea)
        val switchLight = view.findViewById<Switch>(R.id.switchLight)

        // Устанавливаем значения
        switchProtein.isChecked = protein
        switchFast.isChecked = fast
        switchVegan.isChecked = vegan
        switchVegetarian.isChecked = vegetarian
        switchSea.isChecked = sea
        switchLight.isChecked = light

        val saveButton = activity?.findViewById<AppCompatImageButton>(R.id.saveProfileButton)
        saveButton?.visibility = View.GONE

        val onChangeListener = View.OnClickListener {
            showSaveButton()
            protein = switchProtein.isChecked
            fast = switchFast.isChecked
            vegan = switchVegan.isChecked
            vegetarian = switchVegetarian.isChecked
            sea = switchSea.isChecked
            light = switchLight.isChecked
            hasUnsavedChanges = true
        }
        switchProtein.setOnClickListener(onChangeListener)
        switchFast.setOnClickListener(onChangeListener)
        switchVegan.setOnClickListener(onChangeListener)
        switchVegetarian.setOnClickListener(onChangeListener)
        switchSea.setOnClickListener(onChangeListener)
        switchLight.setOnClickListener(onChangeListener)

        saveButton?.setOnClickListener {
            // Сохраняем значения
            editor.putBoolean("protein", protein)
            editor.putBoolean("fast", fast)
            editor.putBoolean("vegan", vegan)
            editor.putBoolean("vegetarian", vegetarian)
            editor.putBoolean("sea", sea)
            editor.putBoolean("light", light)
            editor.apply()
            hasUnsavedChanges = false
            hideSaveButton()
        }
    }

    private fun showSaveButton() {
        activity?.findViewById<AppCompatImageButton>(R.id.saveProfileButton)?.visibility = View.VISIBLE
    }
    private fun hideSaveButton() {
        activity?.findViewById<AppCompatImageButton>(R.id.saveProfileButton)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        if (!hasUnsavedChanges) hideSaveButton()
    }
} 