package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment

class AllergensFragment : Fragment() {
    private var hasUnsavedChanges = false
    private var milk = false
    private var eggs = false
    private var peanut = false
    private var nuts = false
    private var fish = false
    private var seafood = false
    private var gluten = false
    private var fruits = false
    private var chocolate = false
    private var berries = false
    private var vegetables = false
    private var wheat = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_allergens, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireContext().getSharedPreferences("allergens_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        // Получаем сохранённые значения
        milk = sharedPrefs.getBoolean("milk", false)
        eggs = sharedPrefs.getBoolean("eggs", false)
        peanut = sharedPrefs.getBoolean("peanut", false)
        nuts = sharedPrefs.getBoolean("nuts", false)
        fish = sharedPrefs.getBoolean("fish", false)
        seafood = sharedPrefs.getBoolean("seafood", false)
        gluten = sharedPrefs.getBoolean("gluten", false)
        fruits = sharedPrefs.getBoolean("fruits", false)
        chocolate = sharedPrefs.getBoolean("chocolate", false)
        berries = sharedPrefs.getBoolean("berries", false)
        vegetables = sharedPrefs.getBoolean("vegetables", false)
        wheat = sharedPrefs.getBoolean("wheat", false)

        val switchMilk = view.findViewById<Switch>(R.id.switchMilk)
        val switchEggs = view.findViewById<Switch>(R.id.switchEggs)
        val switchPeanut = view.findViewById<Switch>(R.id.switchPeanut)
        val switchNuts = view.findViewById<Switch>(R.id.switchNuts)
        val switchFish = view.findViewById<Switch>(R.id.switchFish)
        val switchSeafood = view.findViewById<Switch>(R.id.switchSeafood)
        val switchGluten = view.findViewById<Switch>(R.id.switchGluten)
        val switchFruits = view.findViewById<Switch>(R.id.switchFruits)
        val switchChocolate = view.findViewById<Switch>(R.id.switchChocolate)
        val switchBerries = view.findViewById<Switch>(R.id.switchBerries)
        val switchVegetables = view.findViewById<Switch>(R.id.switchVegetables)
        val switchWheat = view.findViewById<Switch>(R.id.switchWheat)

        // Устанавливаем значения
        switchMilk.isChecked = milk
        switchEggs.isChecked = eggs
        switchPeanut.isChecked = peanut
        switchNuts.isChecked = nuts
        switchFish.isChecked = fish
        switchSeafood.isChecked = seafood
        switchGluten.isChecked = gluten
        switchFruits.isChecked = fruits
        switchChocolate.isChecked = chocolate
        switchBerries.isChecked = berries
        switchVegetables.isChecked = vegetables
        switchWheat.isChecked = wheat

        val saveButton = activity?.findViewById<AppCompatImageButton>(R.id.saveProfileButton)
        saveButton?.visibility = View.GONE

        val onChangeListener = View.OnClickListener {
            showSaveButton()
            milk = switchMilk.isChecked
            eggs = switchEggs.isChecked
            peanut = switchPeanut.isChecked
            nuts = switchNuts.isChecked
            fish = switchFish.isChecked
            seafood = switchSeafood.isChecked
            gluten = switchGluten.isChecked
            fruits = switchFruits.isChecked
            chocolate = switchChocolate.isChecked
            berries = switchBerries.isChecked
            vegetables = switchVegetables.isChecked
            wheat = switchWheat.isChecked
            hasUnsavedChanges = true
        }
        switchMilk.setOnClickListener(onChangeListener)
        switchEggs.setOnClickListener(onChangeListener)
        switchPeanut.setOnClickListener(onChangeListener)
        switchNuts.setOnClickListener(onChangeListener)
        switchFish.setOnClickListener(onChangeListener)
        switchSeafood.setOnClickListener(onChangeListener)
        switchGluten.setOnClickListener(onChangeListener)
        switchFruits.setOnClickListener(onChangeListener)
        switchChocolate.setOnClickListener(onChangeListener)
        switchBerries.setOnClickListener(onChangeListener)
        switchVegetables.setOnClickListener(onChangeListener)
        switchWheat.setOnClickListener(onChangeListener)

        saveButton?.setOnClickListener {
            // Сохраняем значения
            editor.putBoolean("milk", milk)
            editor.putBoolean("eggs", eggs)
            editor.putBoolean("peanut", peanut)
            editor.putBoolean("nuts", nuts)
            editor.putBoolean("fish", fish)
            editor.putBoolean("seafood", seafood)
            editor.putBoolean("gluten", gluten)
            editor.putBoolean("fruits", fruits)
            editor.putBoolean("chocolate", chocolate)
            editor.putBoolean("berries", berries)
            editor.putBoolean("vegetables", vegetables)
            editor.putBoolean("wheat", wheat)
            editor.apply()
            hasUnsavedChanges = false
            hideSaveButton()
            android.widget.Toast.makeText(requireContext(), "Аллергены сохранены", android.widget.Toast.LENGTH_SHORT).show()
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